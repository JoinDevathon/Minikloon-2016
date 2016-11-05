package org.devathon.contest2016.mm.utils.serialization;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BukkitDataInputStream extends DataInputStream {
    public BukkitDataInputStream(InputStream in) {
        super(in);
    }

    public Location readLocationCoords(World world) throws IOException {
        return new Location(
                world,
                readDouble(),
                readDouble(),
                readDouble()
        );
    }

    public Vector readVector() throws IOException {
        return new Vector(
                readDouble(),
                readDouble(),
                readDouble()
        );
    }
}
