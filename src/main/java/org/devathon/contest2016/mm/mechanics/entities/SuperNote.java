package org.devathon.contest2016.mm.mechanics.entities;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.NoteBlock;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;
import org.devathon.contest2016.mm.BlockSoundSettings;
import org.devathon.contest2016.mm.MachineWorld;
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
                    Note note = string.getPitch();
                    if(note == null) {
                        world.playSound(getLocation(), Sound.ENTITY_CAT_HURT, Note.natural(1, Note.Tone.C));
                    }
                    else {
                        String sound = world.getSoundSettings().getStringSound();
                        world.playSound(getLocation(), sound, note);
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
                world.getBukkitWorld().spawnParticle(Particle.EXPLOSION_NORMAL, getLocation(), 5, 0.1, 0.1, 0.1);
            } else {
                float pitch = getPitch(collidedBlock);
                world.playSound(getLocation(), soundName, pitch);
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

    private float getPitch(Block collidedBlock) {
        Block under = collidedBlock.getRelative(BlockFace.DOWN);
        if(under != null && under.getType().equals(Material.NOTE_BLOCK)) {
            NoteBlock meta = (NoteBlock) under.getState();
            return MinecraftUtils.getPitch(meta.getNote());
        }
        return MinecraftUtils.getPitch(Note.natural(1, Note.Tone.C));
    }

    public void remove() {
        stand.remove();
        world.despawn(this);
    }

    public static class SuperNoteCodec implements Codec<SuperNote> {
        @Override
        public void writeToStream(SuperNote object, BukkitDataOutputStream stream) throws IOException {
            stream.writeInt(object.stand.getHelmet().getType().getId());
            stream.writeShort(object.stand.getHelmet().getDurability());
            stream.writeLocationCoords(object.stand.getLocation());
            stream.writeVector(object.velocity);
        }
        @Override
        public SuperNote readFromStream(MachineWorld world, BukkitDataInputStream stream) throws IOException {
            ItemStack head = new ItemStack(stream.readInt(), 1, stream.readShort());
            SuperNote superNote = spawn(world, stream.readLocationCoords(world.getBukkitWorld()), head);
            superNote.velocity = stream.readVector();
            return superNote;
        }
    }

    public static SuperNote spawn(MachineWorld world, Location location, ItemStack standHead) {
        ArmorStand stand = MinecraftUtils.spawnBoringArmorStand(location, standHead);
        return new SuperNote(world, stand);
    }
}
