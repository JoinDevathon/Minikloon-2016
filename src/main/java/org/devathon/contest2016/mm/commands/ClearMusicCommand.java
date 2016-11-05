package org.devathon.contest2016.mm.commands;

import org.bukkit.entity.Player;
import org.devathon.contest2016.mm.MachineWorld;
import org.devathon.contest2016.mm.mechanics.entities.MusicEntity;

public class ClearMusicCommand extends AdminCommand {
    private final MachineWorld world;

    public ClearMusicCommand(MachineWorld world) {
        super("musicmachine.clearmusic");
        this.world = world;
    }

    @Override
    protected void execute(Player sender, String[] args) {
        world.getEntities().forEach(MusicEntity::remove);
    }
}
