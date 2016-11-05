package org.devathon.contest2016.mm.mechanics.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class EarTrumpet {
    private static final ItemStack item;
    static {
        item = new ItemStack(Material.BOWL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§e§lEar Trumpet §7(Hold in hand)");
        meta.setLore(Arrays.asList(
                "§7Hold this in your hand to hear all of the music effects in the world"
        ));
        item.setItemMeta(meta);
    }

    public static ItemStack getItem() {
        return item;
    }
}
