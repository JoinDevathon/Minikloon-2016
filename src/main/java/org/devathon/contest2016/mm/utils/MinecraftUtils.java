package org.devathon.contest2016.mm.utils;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Location;
import org.bukkit.Note;
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

    private static final ImmutableMap<Integer, Float> noteBlockIdToPitch = new ImmutableMap.Builder<Integer, Float>()
            .put(0, 0.5f)
            .put(1, 0.533333333f)
            .put(2, 0.566666666f)
            .put(3, 0.6f)
            .put(4, 0.633333333f)
            .put(5, 0.666666666f)
            .put(6, 0.7f)
            .put(7, 0.75f)
            .put(8, 0.8f)
            .put(9, 0.85f)
            .put(10, 0.9f)
            .put(11, 0.95f)
            .put(12, 1.0f)
            .put(13, 1.05f)
            .put(14, 1.1f)
            .put(15, 1.2f)
            .put(16, 1.25f)
            .put(17, 1.33333333f)
            .put(18, 1.4f)
            .put(19, 1.5f)
            .put(20, 1.6f)
            .put(21, 1.7f)
            .put(22, 1.8f)
            .put(23, 1.9f)
            .put(24, 2.0f)
            .build();

    public static final float getPitch(Note note) {
        return noteBlockIdToPitch.get((int) note.getId());
    }
}
