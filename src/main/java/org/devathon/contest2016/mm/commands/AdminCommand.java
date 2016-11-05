package org.devathon.contest2016.mm.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class AdminCommand implements CommandExecutor {
    private final String requiredPermission;

    public AdminCommand(String requiredPermission) {
        this.requiredPermission = requiredPermission;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(! commandSender.hasPermission(requiredPermission)) {
            commandSender.sendMessage("§4You don't have enough permissions to use this command!");
            return true;
        }
        if(! (commandSender instanceof Player)) {
            commandSender.sendMessage("§cYou have to be a player to use this command!");
            return true;
        }
        Player sender = (Player) commandSender;

        execute(sender, strings);

        return true;
    }

    protected abstract void execute(Player sender, String[] args);
}
