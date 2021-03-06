package org.devathon.contest2016.mm.mechanics.items;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.devathon.contest2016.mm.MachineWorld;
import org.devathon.contest2016.mm.mechanics.entities.MusicString;

import java.util.*;

public class StringMaker implements Listener {
    private final MachineWorld world;
    private final Map<UUID, LeashSelection> selections = new HashMap<>();

    public StringMaker(MachineWorld world) {
        this.world = world;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if(e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getHand() != EquipmentSlot.HAND)
           return;

        Block clicked = e.getClickedBlock();

        if(! player.getInventory().getItemInMainHand().isSimilar(item))
            return;

        LeashSelection selection = selections.get(player.getUniqueId());
        if(selection == null) {
            if(! player.getInventory().getItemInMainHand().isSimilar(item))
                return;
            onFirstClick(player, clicked);
            e.setCancelled(true);
        }
        else {
            onSecondClick(player, selection, clicked);
            e.setCancelled(true);
        }
    }

    private Vector firstOffset = new Vector(0, -0.125, -0.1875); // weird offset with leashes and bats -0.694
    private Vector secondOffset = new Vector(0.625, 0.625, -0.5);

    private void onFirstClick(Player player, Block clicked) {
        Location entityLoc = clicked.getLocation().add(0.5, 0, 0.5).add(firstOffset);

        LivingEntity firstDummyEntity = MusicString.createDummyEntity(entityLoc);
        firstDummyEntity.setLeashHolder(player);

        LeashSelection selection = new LeashSelection(player.getUniqueId(), clicked, firstDummyEntity);
        selections.put(player.getUniqueId(), selection);

        player.playSound(player.getLocation(), Sound.BLOCK_SLIME_PLACE, 0.5f, 1.0f);
    }

    private void onSecondClick(Player player, LeashSelection selection, Block clicked) {
        Location entityLoc = clicked.getLocation().add(0.5, 0.5, 0.5).add(secondOffset);

        if(selection.getSelectedBlock().equals(clicked)) {
            player.sendMessage("§cYou can't tie a block with itself!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return;
        }

        LivingEntity firstDummyEntity = selection.getDummyEntity();
        LivingEntity secondDummyEntity = MusicString.createDummyEntity(entityLoc);
        firstDummyEntity.setLeashHolder(secondDummyEntity);

        selections.remove(player.getUniqueId());

        MusicString musicString = new MusicString(world, firstDummyEntity, secondDummyEntity, selection.getSelectedBlock(), clicked);
        world.addEntity(musicString);

        player.playSound(player.getLocation(), Sound.BLOCK_SLIME_PLACE, 0.5f, 0.9f);
    }

    @EventHandler
    public void onDummyDamage(EntityDamageEvent e) {
        for(LeashSelection sel : selections.values()) {
            if(sel.getDummyEntity().equals(e.getEntity())) {
                e.setCancelled(true);
                return;
            }
        }
    }

    private class LeashSelection {
        private final UUID playerId;
        private final Block selectedBlock;
        private final LivingEntity dummyEntity;

        private LeashSelection(UUID playerId, Block selectedBlock, LivingEntity dummyEntity) {
            this.playerId = playerId;
            this.selectedBlock = selectedBlock;
            this.dummyEntity = dummyEntity;
        }

        public UUID getPlayerId() {
            return playerId;
        }

        public Block getSelectedBlock() {
            return selectedBlock;
        }

        public LivingEntity getDummyEntity() {
            return dummyEntity;
        }
    }

    @EventHandler
    public void onRagequit(PlayerQuitEvent e) {
        selections.remove(e.getPlayer().getUniqueId());
    }

    private static final ItemStack item;
    static {
        item = new ItemStack(Material.LEASH);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§e§lString Maker §7(Right-Click a block)");
        meta.setLore(Arrays.asList(
                "§7Steps:",
                "§71. Right-Click a block",
                "§72. Right-Click another block",
                "§73. ???",
                "§74. The blocks are tied together!"
        ));
        item.setItemMeta(meta);
    }

    public static ItemStack getItem() {
        return item;
    }
}
