package org.devathon.contest2016.mm;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class SuperNoteGun implements Listener {
    @EventHandler
    public void onShoot(PlayerInteractEvent e) {

    }

    private static final ItemStack item;
    static {
        item = new ItemStack(Material.REDSTONE_COMPARATOR);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§e§lSuper Note Spawner §7(Right-Click)");
        meta.setLore(Arrays.asList("§7Right-click to spawn super notes"));
    }

    public static ItemStack getItem() {
        return item;
    }
}
