package org.devathon.contest2016.mm.mechanics;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.devathon.contest2016.mm.MachineWorld;
import org.devathon.contest2016.mm.NotePitch;
import org.devathon.contest2016.mm.utils.Cooldown;
import org.devathon.contest2016.mm.utils.MinecraftUtils;

public class SuperNote extends CustomEntity {
    private final ArmorStand stand;
    private boolean spawned = true;

    private static final Vector headOffset = new Vector(0, 1.6, 0);

    private boolean collidingGround = false;
    private boolean wasOnGround = false;
    private Cooldown collideCd = new Cooldown(1000);

    private Vector velocity = new Vector(0, 0, 0);
    private static final double maxVelocitySquared = 16 * 16;

    private SuperNote(MachineWorld world, ArmorStand stand) {
        super(world);
        this.stand = stand;
    }

    public Location getLocation() {
        return stand.getLocation().add(headOffset);
    }

    public void setLocation(Location location) {
        stand.teleport(location.subtract(headOffset));
    }

    private static final Vector gravity = new Vector(0, -4.5, 0);
    public void tick(double dSeconds) {
        boolean onGround = checkIsOnGround();
        collidingGround = !wasOnGround && onGround && !collidingGround;
        wasOnGround = onGround;

        if(onGround) {
            velocity.setY(0);
        } else {
            addVelocity(gravity.clone().multiply(dSeconds));
        }

        if(collideCd.isReady()) {
            world.getEntities().stream().filter(e -> e instanceof MusicString).forEach(e -> {
                MusicString string = (MusicString) e;
                if (string.getLineSegment().distanceWithPoint(getLocation().toVector()) < 0.4) {
                    velocity.setY(6.5);
                    NotePitch pitch = string.getPitch();
                    if(pitch == null) {
                        world.playNote(getLocation(), Sound.ENTITY_CAT_HURT, NotePitch.H_DO);
                    }
                    else {
                        world.playNote(getLocation(), Sound.BLOCK_NOTE_HARP, pitch);
                    }
                    collideCd.use();
                }
            });
        }

        Vector deltaMove = velocity.clone().multiply(dSeconds);
        setLocation(getLocation().add(deltaMove));

        if(collidingGround) {
            world.playNote(getLocation(), Sound.BLOCK_NOTE_PLING, NotePitch.M_DO);
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
        ArmorStand stand = MinecraftUtils.spawnBoringArmorStand(location, standHead);
        return new SuperNote(world, stand);
    }
}
