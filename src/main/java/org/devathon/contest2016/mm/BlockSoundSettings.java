package org.devathon.contest2016.mm;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BlockSoundSettings {
    private final String stringSound;
    private final Map<Integer, String> idToSounds;

    public BlockSoundSettings(String stringSound, Map<Integer, String> idToSounds) {
        this.stringSound = stringSound;
        this.idToSounds = idToSounds;
    }

    public String getStringSound() {
        return stringSound;
    }

    public String getSound(Material material) {
        return idToSounds.get(material.getId());
    }

    public List<Material> getAvailableTypes() {
        return idToSounds.keySet().stream()
                .map(Material::getMaterial)
                .collect(Collectors.toList());
    }

    public static BlockSoundSettings parse(ConfigurationSection section) {
        String stringSound = section.getString("string");
        Map<Integer, String> idToSounds = new HashMap<>();
        for(String key : section.getConfigurationSection("blocks").getKeys(false)) {
            int id = Integer.parseInt(key);
            String sound = section.getString("blocks." + key);
            idToSounds.put(id, sound);
        }
        return new BlockSoundSettings(stringSound, idToSounds);
    }
}
