package org.devathon.contest2016.mm.commands;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class ItemGivingCommand extends AdminCommand {
    private final Supplier<ItemStack> giveItem;

    public ItemGivingCommand(String requiredPermission, Supplier<ItemStack> createItem) {
        super(requiredPermission);
        this.giveItem = createItem;
    }

    @Override
    protected void execute(Player sender, String[] args) {
        ItemStack item = giveItem.get();
        sender.getInventory().addItem(item);
        sender.sendMessage("§eGave yourself: " + item.getItemMeta().getDisplayName());
    }
}
