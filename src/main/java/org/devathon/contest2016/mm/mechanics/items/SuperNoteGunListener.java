package org.devathon.contest2016.mm.mechanics.items;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.devathon.contest2016.mm.MachineWorld;
import org.devathon.contest2016.mm.mechanics.entities.SuperNote;

import java.util.Collections;

public class SuperNoteGunListener implements Listener {
    private final MachineWorld mWorld;

    public SuperNoteGunListener(MachineWorld world) {
        this.mWorld = world;
    }

    @EventHandler
    public void onShoot(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if(! player.getInventory().getItemInMainHand().isSimilar(item))
            return;

        Location loc = e.getPlayer().getLocation();

        SuperNote note = SuperNote.spawn(mWorld, loc);
        mWorld.addEntity(note);

        player.sendMessage("§eNote!");

        e.setCancelled(true);
    }

    private static final ItemStack item;
    static {
        item = new ItemStack(Material.REDSTONE_COMPARATOR);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§e§lSuper Note Spawner §7(Right-Click)");
        meta.setLore(Collections.singletonList("§7Right-click to spawn super notes"));
        item.setItemMeta(meta);
    }

    public static ItemStack getItem() {
        return item;
    }
}
