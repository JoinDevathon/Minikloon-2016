package org.devathon.contest2016.mm;

import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.devathon.contest2016.mm.commands.SuperNoteCommand;
import org.devathon.contest2016.mm.mechanics.SuperNoteGunListener;

public class MusicMachine extends JavaPlugin {
    private MachineWorld machineWorld;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        getCommand("supernote").setExecutor(new SuperNoteCommand());

        String worldName = getConfig().getString("world");
        World bukkitWorld = getServer().getWorld(worldName);
        machineWorld = new MachineWorld(bukkitWorld);

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new SuperNoteGunListener(machineWorld), this);

        machineWorld.startTicking(this);
    }

    @Override
    public void onDisable() {
        machineWorld.stopAndDestroy();
    }
}

