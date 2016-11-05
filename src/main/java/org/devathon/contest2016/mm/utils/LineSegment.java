package org.devathon.contest2016.mm.utils;

import org.bukkit.util.Vector;

public class LineSegment {
    private final Vector first;
    private final Vector second;

    private final double length;

    public LineSegment(Vector first, Vector second) {
        this.first = first;
        this.second = second;
        this.length = second.distance(first);
    }

    public double distanceWithPoint(Vector point) {
        Vector v = second.clone().subtract(first);
        Vector w = point.clone().subtract(first);
        double c1 = v.clone().dot(w);
        if(c1 <= 0)
            return point.distance(first);
        double c2 = v.clone().dot(v);
        if(c2 <= c1)
            return point.distance(second);

        double b = c1 / c2;
        Vector pb = first.clone().add(v.clone().multiply(b));
        return point.distance(pb);
    }

    public Vector getFirst() {
        return first.clone();
    }

    public Vector getSecond() {
        return second.clone();
    }

    public double getLength() {
        return length;
    }
}
