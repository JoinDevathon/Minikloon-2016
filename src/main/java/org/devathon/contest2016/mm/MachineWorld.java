package org.devathon.contest2016.mm;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.devathon.contest2016.mm.mechanics.SuperNote;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MachineWorld {
    private final World world;

    private final Set<SuperNote> customEntities = new HashSet<>();
    private BukkitTask tickLoop;

    public MachineWorld(World world) {
        this.world = world;
    }

    public SuperNote spawnSuperNote(Location location) {
        location = location.clone();
        location.setWorld(this.world);
        SuperNote note = SuperNote.spawn(this, location);
        this.customEntities.add(note);
        return note;
    }

    public void despawn(SuperNote entity) {
        entity.remove();
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

        List<SuperNote> entities = new ArrayList<>(customEntities);
        customEntities.clear();
        entities.forEach(SuperNote::remove);
    }
}
