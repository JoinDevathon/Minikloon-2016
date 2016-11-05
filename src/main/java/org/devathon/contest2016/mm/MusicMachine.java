package org.devathon.contest2016.mm;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.devathon.contest2016.mm.commands.SuperNoteCommand;

public class MusicMachine extends JavaPlugin {
    @Override
    public void onEnable() {
        getCommand("supernote").setExecutor(new SuperNoteCommand());
        PluginManager pm = getServer().getPluginManager();
    }
}

