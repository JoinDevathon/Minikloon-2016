package org.devathon.contest2016.mm;

import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.devathon.contest2016.mm.commands.ClearMusicCommand;
import org.devathon.contest2016.mm.commands.ItemGivingCommand;
import org.devathon.contest2016.mm.commands.SampleCommand;
import org.devathon.contest2016.mm.mechanics.items.*;

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

        BlockSoundSettings soundSettings = BlockSoundSettings.parse(getConfig().getConfigurationSection("sounds"));

        String worldName = getConfig().getString("world");
        World bukkitWorld = getServer().getWorld(worldName);
        machineWorld = new MachineWorld(this, bukkitWorld, soundSettings);
        File saveFile = getMusicSaveFile();
        if(saveFile.exists()) {
            try(FileInputStream stream = new FileInputStream(getMusicSaveFile())) {
                machineWorld.setupFromStream(stream);
            } catch (IOException e) {
                getLogger().warning("Couldn't load music machine world save!");
                e.printStackTrace();
            }
        }

        getCommand("supernote").setExecutor(new ItemGivingCommand("musicmachine.supernote", SuperNoteGun::getItem));
        getCommand("stringmaker").setExecutor(new ItemGivingCommand("musicmachine.stringmaker", StringMaker::getItem));
        getCommand("stringcutter").setExecutor(new ItemGivingCommand("musicmachine.stringcutter", StringCutter::getItem));
        getCommand("spawnerwand").setExecutor(new ItemGivingCommand("musicmachine.spawnerwand", SpawnerWand::getItem));
        getCommand("eartrumpet").setExecutor(new ItemGivingCommand("musicmachine.eartrumpet", EarTrumpet::getItem));

        getCommand("clearmusic").setExecutor(new ClearMusicCommand("musicmachine.clearmusic", machineWorld));
        getCommand("sample").setExecutor(new SampleCommand("musicmachine.sample", soundSettings));

        pm.registerEvents(new SuperNoteGun(machineWorld), this);
        pm.registerEvents(new StringMaker(machineWorld), this);
        pm.registerEvents(new StringCutter(machineWorld), this);
        pm.registerEvents(new SpawnerWand(machineWorld), this);

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

