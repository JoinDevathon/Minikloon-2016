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
        double a = (second.clone().subtract(first)).getCrossProduct(first.clone().subtract(point)).lengthSquared();
        double b = second.clone().subtract(first).lengthSquared();
        if(b == 0)
            return 0;
        return Math.sqrt(a/b);
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
