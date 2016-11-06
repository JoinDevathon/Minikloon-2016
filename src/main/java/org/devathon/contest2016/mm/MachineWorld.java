package org.devathon.contest2016.mm;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.devathon.contest2016.mm.mechanics.entities.MusicEntity;
import org.devathon.contest2016.mm.mechanics.entities.MusicEntityType;
import org.devathon.contest2016.mm.mechanics.items.EarTrumpet;
import org.devathon.contest2016.mm.utils.serialization.BukkitDataInputStream;
import org.devathon.contest2016.mm.utils.serialization.BukkitDataOutputStream;
import org.devathon.contest2016.mm.utils.serialization.Codec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class MachineWorld {
    private final JavaPlugin plugin;
    private final World world;

    private final BlockSoundSettings soundSettings;

    private final Set<MusicEntity> customEntities = new HashSet<>();
    private BukkitTask tickLoop;

    public MachineWorld(JavaPlugin plugin, World world, BlockSoundSettings soundSettings) {
        this.plugin = plugin;
        this.world = world;
        this.soundSettings = soundSettings;
    }

    public void addEntity(MusicEntity entity) {
        if(customEntities.add(entity)) {
            plugin.getServer().getPluginManager().registerEvents(entity, plugin);
        }
    }

    public void despawn(MusicEntity entity) {
        if(customEntities.remove(entity)) {
            entity.remove();
            HandlerList.unregisterAll(entity);
        }
    }

    private long lastTickMs = System.currentTimeMillis();
    public void startTicking(Plugin plugin) {
        if(tickLoop != null)
            return;
        tickLoop = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            long elapsedMs = System.currentTimeMillis() - lastTickMs;
            double dSeconds = ((double) elapsedMs) / 1000.0;
            tick(0.05);
            lastTickMs = System.currentTimeMillis();
        }, 0, 1L);
    }

    private void tick(double dSeconds) {
        getEntities().forEach(e -> e.tick(dSeconds));
    }

    public void stopAndDestroy() {
        if(tickLoop != null) {
            tickLoop.cancel();
        }

        List<MusicEntity> entities = new ArrayList<>(customEntities);
        customEntities.clear();
        entities.forEach(MusicEntity::remove);
    }

    public World getBukkitWorld() {
        return world;
    }

    public BlockSoundSettings getSoundSettings() {
        return soundSettings;
    }

    public Collection<MusicEntity> getEntities() {
        return new ArrayList<>(customEntities);
    }
    
    public void playNote(Location loc, Sound sound, NotePitch note) {
        world.getPlayers().forEach(p -> {
            if(p.getInventory().getItemInMainHand().isSimilar(EarTrumpet.getItem())) {
                p.playSound(p.getLocation(), sound, 1.0f, note.getPitch());
            } else {
                p.playSound(loc, sound, 1.0f, note.getPitch());
            }
        });
    }

    public void playNote(Location loc, String sound, NotePitch note) {
        world.getPlayers().forEach(p -> {
            if(p.getInventory().getItemInMainHand().isSimilar(EarTrumpet.getItem())) {
                p.playSound(p.getLocation(), sound, 1.0f, note.getPitch());
            } else {
                p.playSound(loc, sound, 1.0f, note.getPitch());
            }
        });
    }

    public void writeToStream(OutputStream stream) throws IOException {
        BukkitDataOutputStream mcStream = new BukkitDataOutputStream(stream);
        Collection<MusicEntity> entities = getEntities();
        mcStream.writeInt(entities.size());
        entities.forEach(e -> {
            try {
                mcStream.writeInt(e.getType().getId());
                e.getType().getCodec().writeToStream(e, mcStream);
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public void setupFromStream(InputStream stream) throws IOException {
        BukkitDataInputStream mcStream = new BukkitDataInputStream(stream);
        int entitiesCount = mcStream.readInt();
        for(int i = 0; i < entitiesCount; ++i) {
            int typeId = mcStream.readInt();
            Codec codec = MusicEntityType.idsToCodec.get(typeId);
            MusicEntity entity = codec.readFromStream(this, mcStream);
            addEntity(entity);
        }
    }
}
