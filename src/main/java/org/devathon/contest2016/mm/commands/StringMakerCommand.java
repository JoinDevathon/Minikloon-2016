package org.devathon.contest2016.mm.commands;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.devathon.contest2016.mm.mechanics.StringMaker;

public class StringMakerCommand extends AdminCommand {
    public StringMakerCommand() {
        super("musicmachine.leashmaker");
    }

    @Override
    protected void execute(Player sender, String[] args) {
        ItemStack item = StringMaker.getItem();
        sender.getInventory().addItem(item);
        sender.sendMessage("§eYou gave yourself a leash maker!");
    }
}
