package org.devathon.contest2016.mm.mechanics.items;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.Listener;
import org.devathon.contest2016.mm.MachineWorld;
import org.devathon.contest2016.mm.mechanics.entities.NoteSpawner;

import java.util.Arrays;
import java.util.Optional;

public class SpawnerWand implements Listener {
    private final MachineWorld world;

    public SpawnerWand(MachineWorld world) {
        this.world = world;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if(! player.getInventory().getItemInMainHand().isSimilar(item))
            return;
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK || !e.getHand().equals(EquipmentSlot.HAND))
            return;

        Block clicked = e.getClickedBlock();

        Optional<NoteSpawner> existing = world.getEntities().stream()
                .filter(en -> en instanceof NoteSpawner)
                .map(en -> (NoteSpawner) en)
                .filter(spawner -> spawner.getBlockLocation().equals(clicked.getLocation()))
                .findFirst();

        if(existing.isPresent()) {
            onChangeFrequency(player, existing.get());
        }
        else {
            Location clickedLoc = clicked.getLocation();
            int defaultFrequency = 2000;
            NoteSpawner spawner = new NoteSpawner(world, clickedLoc, defaultFrequency);
            world.addEntity(spawner);

            player.sendMessage("§aThe block at " + clicked.getX() + ", " + clicked.getY() + ", " + clicked.getZ() + " will now spawn notes every " + defaultFrequency + "ms!");
            player.sendMessage("§aClick it again to change the frequency!");
        }
    }

    private void onChangeFrequency(Player player, NoteSpawner spawner) {
        long frequencyMs = spawner.getFrequencyMs();
        int increments = 50;
        int min = 50;
        int max = 16000;
        if(player.isSneaking()) {
            frequencyMs -= increments;
            if(frequencyMs < min)
                frequencyMs = max;
        }
        else {
            frequencyMs += increments;
            if(frequencyMs > max)
                frequencyMs = min;
        }
        spawner.setFrequencyMs(frequencyMs);
        player.sendMessage("§aSpawning frequency: " + frequencyMs + "ms");
    }

    private static final ItemStack item;
    static {
        item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§e§lSuper Note Spawner Wand §7(Right-Click a block)");
        meta.setLore(Arrays.asList(
                "§7Right-click a block to transform it into a spawner",
                "§7Right-click it again to increase its frequency",
                "§7Right-click + Shift to decrease its frequency"
        ));
        item.setItemMeta(meta);
    }

    public static ItemStack getItem() {
        return item;
    }
}
