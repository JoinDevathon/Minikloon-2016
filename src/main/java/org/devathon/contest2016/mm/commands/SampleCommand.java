package org.devathon.contest2016.mm.commands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
        List<Material> availableType = settings.getAvailableTypes();

        for(Material mat : availableType) {
            String sound = settings.getSound(mat);

            ItemStack item = new ItemStack(mat);
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
