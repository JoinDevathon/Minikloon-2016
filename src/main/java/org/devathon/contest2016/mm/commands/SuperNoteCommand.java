package org.devathon.contest2016.mm.commands;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.devathon.contest2016.mm.mechanics.SuperNoteGunListener;

public class SuperNoteCommand extends AdminCommand {
    public SuperNoteCommand() {
        super("musicmachine.supernote");
    }

    @Override
    protected void execute(Player sender, String[] args) {
        ItemStack gun = SuperNoteGunListener.getItem();
        sender.getInventory().addItem(gun);
        sender.sendMessage("§eYou gave yourself a super note gun!");
    }
}

