package org.devathon.contest2016.mm;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.devathon.contest2016.mm.mechanics.CustomEntity;
import org.devathon.contest2016.mm.utils.MathStuff;

import java.util.*;

public class MachineWorld {
    private final JavaPlugin plugin;
    private final World world;

    private final Set<CustomEntity> customEntities = new HashSet<>();
    private BukkitTask tickLoop;

    public MachineWorld(JavaPlugin plugin, World world) {
        this.plugin = plugin;
        this.world = world;
    }

    public void addEntity(CustomEntity entity) {
        if(customEntities.add(entity)) {
            plugin.getServer().getPluginManager().registerEvents(entity, plugin);
        }
    }

    public void despawn(CustomEntity entity) {
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
            tick(dSeconds);
            lastTickMs = System.currentTimeMillis();
        }, 0, 1L);
    }

    private void tick(double dSeconds) {
        customEntities.forEach(e -> e.tick(dSeconds));
    }

    public void stopAndDestroy() {
        if(tickLoop != null) {
            tickLoop.cancel();
        }

        List<CustomEntity> entities = new ArrayList<>(customEntities);
        customEntities.clear();
        entities.forEach(CustomEntity::remove);
    }

    public Collection<CustomEntity> getEntities() {
        return new ArrayList<>(customEntities);
    }

    public Vector getBounceDirection(Location loc, Vector velocity) {
        return MathStuff.getBounceDirection(world, loc, velocity);
    }

    public void playNote(Location loc, Sound sound, NotePitch note) {
        world.getPlayers().forEach(p -> {
            p.playSound(loc, sound, 1.0f, note.getPitch());
        });
    }
}
