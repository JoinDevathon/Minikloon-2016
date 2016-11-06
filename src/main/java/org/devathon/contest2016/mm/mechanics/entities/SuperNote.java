package org.devathon.contest2016.mm.mechanics.entities;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;
import org.devathon.contest2016.mm.BlockSoundSettings;
import org.devathon.contest2016.mm.MachineWorld;
import org.devathon.contest2016.mm.NotePitch;
import org.devathon.contest2016.mm.utils.Cooldown;
import org.devathon.contest2016.mm.utils.MinecraftUtils;
import org.devathon.contest2016.mm.utils.serialization.BukkitDataInputStream;
import org.devathon.contest2016.mm.utils.serialization.BukkitDataOutputStream;
import org.devathon.contest2016.mm.utils.serialization.Codec;

import java.io.IOException;

public class SuperNote extends MusicEntity {
    private final ArmorStand stand;

    private static final Vector headOffset = new Vector(0, 1.6, 0);

    private boolean collidingGround = false;
    private boolean wasOnGround = false;
    private Cooldown collideCd = new Cooldown(250);

    private Vector velocity = new Vector(0, 0, 0);

    private SuperNote(MachineWorld world, ArmorStand stand) {
        super(world);
        this.stand = stand;
    }

    @Override
    public MusicEntityType getType() {
        return MusicEntityType.SUPER_NOTE;
    }

    public Location getLocation() {
        return stand.getLocation().add(headOffset);
    }

    public void setLocation(Location location) {
        stand.teleport(location.subtract(headOffset));
    }

    private static final Vector gravity = new Vector(0, -16, 0);
    public void tick(double dSeconds) {
        boolean onGround = checkIsOnGround();
        collidingGround = !wasOnGround && onGround && !collidingGround;
        wasOnGround = onGround;

        if(onGround) {
            velocity.setY(0);
        } else {
            velocity.add(gravity.clone().multiply(dSeconds));
        }

        if(collideCd.isReady()) {
            world.getEntities().stream().filter(e -> e instanceof MusicString).forEach(e -> {
                MusicString string = (MusicString) e;
                if (string.getLineSegment().distanceWithPoint(getLocation().toVector()) < 0.75) {
                    velocity = string.getBounceVelocity(12.0, velocity);
                    NotePitch pitch = string.getPitch();
                    if(pitch == null) {
                        world.playNote(getLocation(), Sound.ENTITY_CAT_HURT, NotePitch.H_DO);
                    }
                    else {
                        String sound = world.getSoundSettings().getStringSound();
                        world.playNote(getLocation(), sound, pitch);
                    }
                    collideCd.use();
                }
            });
        }

        if(collidingGround) {
            Block collidedBlock = getLocation().getBlock();
            MaterialData collidedType = new MaterialData(collidedBlock.getType(), collidedBlock.getData());
            BlockSoundSettings soundSettings = world.getSoundSettings();
            String soundName = soundSettings.getSound(collidedType);
            if(soundName == null) {
                remove();
                if(! soundSettings.isMuted(collidedType)) {
                    world.getBukkitWorld().playEffect(getLocation(), Effect.EXTINGUISH, 0);
                }
            } else {
                world.playNote(getLocation(), soundName, NotePitch.M_DO);
                velocity.setY(12.0);
            }
        }

        Vector deltaMove = velocity.clone().multiply(dSeconds);
        setLocation(getLocation().add(deltaMove));
    }

    private boolean checkIsOnGround() {
        Block under = getLocation().getBlock();
        return under.getType() != Material.AIR;
    }

    public void remove() {
        stand.remove();
        world.despawn(this);
    }

    public static class SuperNoteCodec implements Codec<SuperNote> {
        @Override
        public void writeToStream(SuperNote object, BukkitDataOutputStream stream) throws IOException {
            stream.writeLocationCoords(object.stand.getLocation());
            stream.writeVector(object.velocity);
        }
        @Override
        public SuperNote readFromStream(MachineWorld world, BukkitDataInputStream stream) throws IOException {
            SuperNote superNote = spawn(world, stream.readLocationCoords(world.getBukkitWorld()));
            superNote.velocity = stream.readVector();
            return superNote;
        }
    }

    private static final ItemStack standHead = new ItemStack(Material.IRON_BLOCK);
    public static SuperNote spawn(MachineWorld world, Location location) {
        ArmorStand stand = MinecraftUtils.spawnBoringArmorStand(location, standHead);
        return new SuperNote(world, stand);
    }
}
