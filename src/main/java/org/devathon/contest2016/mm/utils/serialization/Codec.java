package org.devathon.contest2016.mm.utils.serialization;

import org.devathon.contest2016.mm.MachineWorld;
import org.devathon.contest2016.mm.mechanics.MusicEntity;

import java.io.IOException;

public interface Codec<T extends MusicEntity> {
    void writeToStream(T object, BukkitDataOutputStream stream) throws IOException;

    T readFromStream(MachineWorld world, BukkitDataInputStream stream) throws IOException;
}
