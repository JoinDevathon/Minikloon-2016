package org.devathon.contest2016.mm.mechanics;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.devathon.contest2016.mm.MachineWorld;
import org.devathon.contest2016.mm.NotePitch;

public class SuperNote {
    private final MachineWorld world;
    private final ArmorStand stand;
    private boolean spawned = true;

    private NotePitch notePitch = NotePitch.M_DO;

    private static final Vector headOffset = new Vector(0, 1.6, 0);

    private boolean colliding = false;
    private boolean wasOnGround = false;

    private Vector velocity = new Vector(0, 0, 0);
    private static final double maxVelocitySquared = 10 * 10;

    private SuperNote(MachineWorld world, ArmorStand stand) {
        this.world = world;
        this.stand = stand;
    }

    public Location getLocation() {
        return stand.getLocation().add(headOffset);
    }

    public void setLocation(Location location) {
        stand.teleport(location.subtract(headOffset));
    }

    public void setNotePitch(NotePitch notePitch) {
        this.notePitch = notePitch;
    }

    private static final Vector gravity = new Vector(0, -0.7, 0);
    public void tick(double dSeconds) {
        boolean onGround = checkIsOnGround();
        colliding = !wasOnGround && onGround && !colliding;
        wasOnGround = onGround;

        if(onGround) {
            velocity.setY(0);
        } else {
            addVelocity(gravity);
        }

        Vector deltaMove = velocity.clone().multiply(dSeconds);
        setLocation(getLocation().add(deltaMove));

        if(colliding) {
            world.playNote(getLocation(), Sound.BLOCK_NOTE_HARP, notePitch);
        }
    }

    private void addVelocity(Vector add) {
        if(velocity.clone().add(add).lengthSquared() < maxVelocitySquared) {
            velocity.add(add);
        }
    }

    private boolean checkIsOnGround() {
        Block under = getLocation().getBlock();
        return under.getType() != Material.AIR;
    }

    public void remove() {
        if(!spawned) return;
        spawned = false;
        stand.remove();
        world.despawn(this);
    }

    private static final ItemStack standHead = new ItemStack(Material.NOTE_BLOCK);
    public static SuperNote spawn(MachineWorld world, Location location) {
        ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        stand.setAI(false);
        stand.setVisible(false);
        stand.setGravity(false);
        stand.setBasePlate(false);
        stand.setHelmet(standHead);
        return new SuperNote(world, stand);
    }
}
