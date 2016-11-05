package org.devathon.contest2016.mm.mechanics;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.devathon.contest2016.mm.MachineWorld;

public class SuperNote {
    private final MachineWorld world;
    private final ArmorStand stand;
    private boolean spawned = true;

    private SuperNote(MachineWorld world, ArmorStand stand) {
        this.world = world;
        this.stand = stand;
    }

    public Location getLocation() {
        return stand.getLocation(); // TODO: Head offset
    }

    public void tick(double dSeconds) {

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
