package org.devathon.contest2016.mm.utils.serialization;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BukkitDataOutputStream extends DataOutputStream {
    public BukkitDataOutputStream(OutputStream out) {
        super(out);
    }

    public void writeLocationCoords(Location location) throws IOException {
        writeDouble(location.getX());
        writeDouble(location.getY());
        writeDouble(location.getZ());
    }

    public void writeVector(Vector vector) throws IOException {
        writeDouble(vector.getX());
        writeDouble(vector.getY());
        writeDouble(vector.getZ());
    }
}
