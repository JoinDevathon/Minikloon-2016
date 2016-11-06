package org.devathon.contest2016.mm.commands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.devathon.contest2016.mm.BlockSoundSettings;

import java.util.Arrays;
import java.util.List;

public class SampleCommand extends AdminCommand {
    private final BlockSoundSettings settings;

    public SampleCommand(String requiredPermission, BlockSoundSettings settings) {
        super(requiredPermission);
        this.settings = settings;
    }

    @Override
    protected void execute(Player sender, String[] args) {
        List<MaterialData> availableType = settings.getAvailableTypes();

        for(MaterialData mat : availableType) {
            String sound = settings.getSound(mat);

            ItemStack item = mat.toItemStack();
            item.setAmount(1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§e§lSample block: §b\"" + sound + "\"");
            meta.setLore(Arrays.asList(
                    "§7Blocks of this type will produce the sound",
                    "§7" + sound,
                    "§7when hit by a super note!"
            ));
            item.setItemMeta(meta);

            sender.getInventory().addItem(item);
        }
    }
}
