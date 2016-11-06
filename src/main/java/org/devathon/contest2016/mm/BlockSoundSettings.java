package org.devathon.contest2016.mm;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.material.MaterialData;

import java.util.*;
import java.util.stream.Collectors;

public class BlockSoundSettings {
    private final String defaultStringSound;
    private final Map<MaterialData, String> idToSounds;
    private final Set<MaterialData> mutedTypes;

    public BlockSoundSettings(String defaultStringSound, Map<MaterialData, String> idToSounds, Set<MaterialData> mutedTypes) {
        this.defaultStringSound = defaultStringSound;
        this.idToSounds = idToSounds;
        this.mutedTypes = mutedTypes;
    }

    public String getDefaultStringSound() {
        return defaultStringSound;
    }

    public String getSound(Material material, byte data) {
        return getSound(new MaterialData(material, data));
    }

    public String getSound(MaterialData materialData) {
        return idToSounds.get(materialData);
    }

    public List<MaterialData> getAvailableTypes() {
        return new ArrayList<>(idToSounds.keySet());
    }

    public boolean isMuted(Block block) {
        return isMuted(new MaterialData(block.getType(), block.getData()));
    }

    public boolean isMuted(MaterialData materialData) {
        return mutedTypes.contains(materialData);
    }

    public static BlockSoundSettings parse(ConfigurationSection section) {
        String stringSound = section.getString("defaultString");
        Map<MaterialData, String> idToSounds = new HashMap<>();
        for(String key : section.getConfigurationSection("blocks").getKeys(false)) {
            MaterialData matAnDat = parseMatData(key);
            String sound = section.getString("blocks." + key);
            idToSounds.put(matAnDat, sound);
        }

        Set<MaterialData> mutedTypes = section.getStringList("muted").stream()
                .map(BlockSoundSettings::parseMatData)
                .collect(Collectors.toSet());

        return new BlockSoundSettings(stringSound, idToSounds, mutedTypes);
    }

    private static MaterialData parseMatData(String str) {
        String[] parts = str.split(":");
        Material mat = Material.getMaterial(Integer.parseInt(parts[0]));
        byte data = parts.length > 1 ? Byte.parseByte(parts[1]) : 0;
        return new MaterialData(mat, data);
    }
}
