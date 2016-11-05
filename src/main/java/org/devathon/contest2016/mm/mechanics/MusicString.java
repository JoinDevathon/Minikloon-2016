package org.devathon.contest2016.mm.mechanics;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.devathon.contest2016.mm.MachineWorld;
import org.devathon.contest2016.mm.NotePitch;
import org.devathon.contest2016.mm.utils.LineSegment;

import java.util.HashMap;
import java.util.Map;

public class MusicString extends CustomEntity {
    private final Entity first;
    private final Entity second;

    private final LineSegment segment;

    public MusicString(MachineWorld world, Entity first, Entity second, Block firstBlock, Block secondBlock) {
        super(world);
        this.first = first;
        this.second = second;
        segment = new LineSegment(firstBlock.getLocation().add(0.5, 0.5, 0.5).toVector(), secondBlock.getLocation().add(0.5, 0.5, 0.5).toVector());
    }

    @Override
    public void tick(double dSeconds) {
    }

    public LineSegment getLineSegment() {
        return segment;
    }

    public NotePitch getPitch() {
        int length = (int) segment.getLength();
        return lengthPitches.get(length);
    }

    private static final Map<Integer, NotePitch> lengthPitches = new HashMap<>();
    static {
        lengthPitches.put(1, NotePitch.L_SI);
        lengthPitches.put(2, NotePitch.M_DO);
        lengthPitches.put(3, NotePitch.M_RE);
        lengthPitches.put(4, NotePitch.M_MI);
        lengthPitches.put(5, NotePitch.M_FA);
        lengthPitches.put(6, NotePitch.M_SOL);
        lengthPitches.put(7, NotePitch.M_LA);
        lengthPitches.put(8, NotePitch.M_SI);
        lengthPitches.put(9, NotePitch.H_DO);
        lengthPitches.put(10, NotePitch.H_RE);
    }

    @Override
    public void remove() {
        first.remove();
        second.remove();
        world.despawn(this);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        Entity damaged = e.getEntity();
        if(damaged.equals(first) || damaged.equals(second)) {
            e.setCancelled(true);
        }
    }
}
