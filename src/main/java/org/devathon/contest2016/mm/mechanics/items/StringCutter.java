package org.devathon.contest2016.mm.mechanics.items;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.Listener;
import org.devathon.contest2016.mm.MachineWorld;
import org.devathon.contest2016.mm.mechanics.entities.MusicString;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class StringCutter implements Listener {
    private final MachineWorld world;
    private static final HashSet<Action> acceptedActions = new HashSet<>(Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK));

    public StringCutter(MachineWorld world) {
        this.world = world;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if(! player.getInventory().getItemInMainHand().isSimilar(item))
            return;
        if(! acceptedActions.contains(e.getAction()) || e.getHand() != EquipmentSlot.HAND)
            return;

        List<MusicString> cut = world.getEntities().stream()
                .filter(en -> en instanceof MusicString)
                .map(en -> (MusicString) en)
                .filter(string -> string.getLineSegment().distanceWithPoint(e.getPlayer().getEyeLocation().toVector()) < 2)
                .collect(Collectors.toList());

        if(cut.isEmpty()) {
            player.sendMessage("§cNo nearby string to cut!");
        } else {
            player.sendMessage("§aYou have cut " + cut.size() + " string" + (cut.size() == 1 ? "" : "s") + "!");
            player.playSound(player.getLocation(), Sound.ENTITY_SHEEP_SHEAR, 1.0f, 1.0f);
        }

        cut.forEach(MusicString::remove);
    }

    private static final ItemStack item;
    static {
        item = new ItemStack(Material.SHEARS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§e§lMusic String Cutter §7(Right-Click)");
        meta.setLore(Collections.singletonList("§7Right-click near a music string to cut it"));
        item.setItemMeta(meta);
    }

    public static ItemStack getItem() {
        return item;
    }
}
