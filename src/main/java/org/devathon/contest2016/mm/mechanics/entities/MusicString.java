package org.devathon.contest2016.mm.mechanics.entities;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.devathon.contest2016.mm.MachineWorld;
import org.devathon.contest2016.mm.NotePitch;
import org.devathon.contest2016.mm.utils.LineSegment;
import org.devathon.contest2016.mm.utils.serialization.BukkitDataInputStream;
import org.devathon.contest2016.mm.utils.serialization.BukkitDataOutputStream;
import org.devathon.contest2016.mm.utils.serialization.Codec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MusicString extends MusicEntity {
    private final Entity first;
    private final Entity second;
    private boolean spawned = false;

    private final LineSegment segment;

    public MusicString(MachineWorld world, Entity first, Entity second, Block firstBlock, Block secondBlock) {
        super(world);
        this.first = first;
        this.second = second;
        segment = new LineSegment(firstBlock.getLocation().add(0.5, 0.5, 0.5).toVector(), secondBlock.getLocation().add(0.5, 0.5, 0.5).toVector());
    }

    @Override
    public MusicEntityType getType() {
        return MusicEntityType.MUSIC_STRING;
    }

    @Override
    public void tick(double dSeconds) {
    }

    public LineSegment getLineSegment() {
        return segment;
    }

    public Vector getBounceVelocity(double pseudoForce) {
        Vector first = segment.getFirst();
        Vector second = segment.getSecond();
        if(first.getY() > second.getY()) {
            first = second;
            second = segment.getFirst();
        }

        Vector direction = second.clone().subtract(first);
        Vector directionXZ = direction.clone().setY(0).normalize();
        double ratio = direction.getY() / direction.clone().setY(0).length();
        directionXZ.multiply(pseudoForce * -ratio);
        directionXZ.setY(pseudoForce);

        return directionXZ;
    }

    public NotePitch getPitch() {
        int length = (int) segment.getLength();
        return lengthPitches.get(length);
    }

    private static final Map<Integer, NotePitch> lengthPitches = new HashMap<>();
    static {
        lengthPitches.put(1, NotePitch.L_SI);
        lengthPitches.put(2, NotePitch.M_DO);
        lengthPitches.put(3, NotePitch.M_RE);
        lengthPitches.put(4, NotePitch.M_MI);
        lengthPitches.put(5, NotePitch.M_FA);
        lengthPitches.put(6, NotePitch.M_SOL);
        lengthPitches.put(7, NotePitch.M_LA);
        lengthPitches.put(8, NotePitch.M_SI);
        lengthPitches.put(9, NotePitch.H_DO);
        lengthPitches.put(10, NotePitch.H_RE);
        lengthPitches.put(11, NotePitch.H_MI);
    }

    @Override
    public void remove() {
        first.remove();
        second.remove();
        world.despawn(this);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        Entity damaged = e.getEntity();
        if(damaged.equals(first) || damaged.equals(second)) {
            e.setCancelled(true);
        }
    }

    public static class MusicStringCodec implements Codec<MusicString> {
        @Override
        public void writeToStream(MusicString object, BukkitDataOutputStream stream) throws IOException {
            stream.writeLocationCoords(object.first.getLocation());
            stream.writeLocationCoords(object.second.getLocation());
            stream.writeVector(object.segment.getFirst());
            stream.writeVector(object.segment.getSecond());
        }
        @Override
        public MusicString readFromStream(MachineWorld world, BukkitDataInputStream stream) throws IOException {
            Location firstEntityLoc = stream.readLocationCoords(world.getBukkitWorld());
            Location secondEntityLoc = stream.readLocationCoords(world.getBukkitWorld());
            Location segmentA = stream.readLocationCoords(world.getBukkitWorld());
            Location segmentB = stream.readLocationCoords(world.getBukkitWorld());
            LivingEntity firstDummyEntity = createDummyEntity(firstEntityLoc);
            LivingEntity secondDummyEntity = createDummyEntity(secondEntityLoc);
            firstDummyEntity.setLeashHolder(secondDummyEntity);
            return new MusicString(world, firstDummyEntity, secondDummyEntity, segmentA.getBlock(), segmentB.getBlock());
        }
    }

    public static LivingEntity createDummyEntity(Location loc) {
        Bat dummy = (Bat) loc.getWorld().spawnEntity(loc, EntityType.BAT);
        dummy.setAI(false);
        dummy.setGravity(false);
        dummy.setSilent(true);
        dummy.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
        return dummy;
    }
}
