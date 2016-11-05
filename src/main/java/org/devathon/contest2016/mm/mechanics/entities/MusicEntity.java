package org.devathon.contest2016.mm.mechanics.entities;

import org.bukkit.event.Listener;
import org.devathon.contest2016.mm.MachineWorld;

public abstract class MusicEntity implements Listener {
    protected final MachineWorld world;

    public MusicEntity(MachineWorld world) {
        this.world = world;
    }

    public abstract MusicEntityType getType();

    public abstract void tick(double dSeconds);

    public abstract void remove();
}
