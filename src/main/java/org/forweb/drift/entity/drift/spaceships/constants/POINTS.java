package org.forweb.drift.entity.drift.spaceships.constants;

import org.forweb.geometry.shapes.Point;

public enum POINTS {

    FALCON(new Point[]{
            new Point(-10d, -10d), new Point(-10d, 10d), new Point(20, 0)
    });

    public final Point[] points;

    private POINTS(Point[] points) {
        this.points = points;
    }

}
