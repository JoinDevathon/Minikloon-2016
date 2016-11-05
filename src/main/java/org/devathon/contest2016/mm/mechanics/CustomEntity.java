package org.devathon.contest2016.mm.mechanics;

import org.bukkit.event.Listener;
import org.devathon.contest2016.mm.MachineWorld;

public abstract class CustomEntity implements Listener {
    protected final MachineWorld world;

    public CustomEntity(MachineWorld world) {
        this.world = world;
    }

    public abstract void tick(double dSeconds);

    public abstract void remove();
}
