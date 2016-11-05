package org.devathon.contest2016.mm.utils;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public final class MinecraftUtils {
    public static ArmorStand spawnBoringArmorStand(Location location, ItemStack head) {
        ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        stand.setAI(false);
        stand.setVisible(false);
        stand.setGravity(false);
        stand.setBasePlate(false);
        stand.setHelmet(head);
        return stand;
    }
}
