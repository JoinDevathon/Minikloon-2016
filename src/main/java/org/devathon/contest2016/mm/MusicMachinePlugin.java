package org.devathon.contest2016.mm;

import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.devathon.contest2016.mm.commands.ClearMusicCommand;
import org.devathon.contest2016.mm.commands.StringMakerCommand;
import org.devathon.contest2016.mm.commands.SuperNoteCommand;
import org.devathon.contest2016.mm.mechanics.StringMaker;
import org.devathon.contest2016.mm.mechanics.SuperNoteGunListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

public class MusicMachinePlugin extends JavaPlugin {
    private MachineWorld machineWorld;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        PluginManager pm = getServer().getPluginManager();

        String worldName = getConfig().getString("world");
        World bukkitWorld = getServer().getWorld(worldName);
        machineWorld = new MachineWorld(this, bukkitWorld);
        File saveFile = getMusicSaveFile();
        if(saveFile.exists()) {
            try(FileInputStream stream = new FileInputStream(getMusicSaveFile())) {
                machineWorld.setupFromStream(stream);
            } catch (IOException e) {
                getLogger().warning("Couldn't load music machine world save!");
                e.printStackTrace();
            }
        }

        getCommand("supernote").setExecutor(new SuperNoteCommand());
        getCommand("stringmaker").setExecutor(new StringMakerCommand());
        getCommand("clearmusic").setExecutor(new ClearMusicCommand(machineWorld));

        pm.registerEvents(new SuperNoteGunListener(machineWorld), this);
        pm.registerEvents(new StringMaker(machineWorld), this);

        machineWorld.startTicking(this);
    }

    @Override
    public void onDisable() {
        try(FileOutputStream stream = new FileOutputStream(getMusicSaveFile())) {
            machineWorld.writeToStream(stream);
        } catch (IOException e) {
            getLogger().warning("Couldn't save music machine world save!");
            e.printStackTrace();
        }
        machineWorld.stopAndDestroy();
    }

    public File getMusicSaveFile() {
        return Paths.get(machineWorld.getBukkitWorld().getWorldFolder().getAbsolutePath(), "musicmachine.bin").toFile();
    }
}

