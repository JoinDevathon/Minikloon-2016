package org.devathon.contest2016.mm.mechanics.entities;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.devathon.contest2016.mm.MachineWorld;
import org.devathon.contest2016.mm.utils.Cooldown;
import org.devathon.contest2016.mm.utils.serialization.BukkitDataInputStream;
import org.devathon.contest2016.mm.utils.serialization.BukkitDataOutputStream;
import org.devathon.contest2016.mm.utils.serialization.Codec;

import java.io.IOException;

public class NoteSpawner extends MusicEntity {
    private final Location blockLocation;
    private Cooldown cooldown;

    public NoteSpawner(MachineWorld world, Location blockLocation, long frequencyMs) {
        super(world);
        this.blockLocation = blockLocation;
        this.cooldown = new Cooldown(frequencyMs);
    }

    @Override
    public MusicEntityType getType() {
        return MusicEntityType.SUPER_NOTE_SPAWNER;
    }

    public Location getBlockLocation() {
        return blockLocation;
    }

    public long getFrequencyMs() {
        return cooldown.getCooldownMs();
    }

    public void setFrequencyMs(long frequencyMs) {
        cooldown = new Cooldown(frequencyMs);
        cooldown.use();
    }

    @Override
    public void tick(double dSeconds) {
        if(! cooldown.isReady())
            return;
        cooldown.use();

        Location spawnLocation = blockLocation.clone().add(0.5, -2, 0.5);
        SuperNote note = SuperNote.spawn(world, spawnLocation);
        world.addEntity(note);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if(! e.getBlock().getLocation().equals(blockLocation))
            return;
        remove();
        e.getPlayer().sendMessage("§aYou have removed a super note spawner!");
    }

    @Override
    public void remove() {
        world.despawn(this);
    }

    public static class SuperNoteSpawnerCodec implements Codec<NoteSpawner> {
        @Override
        public void writeToStream(NoteSpawner object, BukkitDataOutputStream stream) throws IOException {
            stream.writeLocationCoords(object.blockLocation);
            stream.writeLong(object.getFrequencyMs());
        }

        @Override
        public NoteSpawner readFromStream(MachineWorld world, BukkitDataInputStream stream) throws IOException {
            return new NoteSpawner(
                    world,
                    stream.readLocationCoords(world.getBukkitWorld()),
                    stream.readLong()
            );
        }
    }
}
