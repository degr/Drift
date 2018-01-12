package org.forweb.drift.entity.drift.spaceships.constants;

import org.jbox2d.common.Vec2;

public enum POINTS {

    FALCON(new Vec2[]{
            new Vec2(-10d, -10d), new Vec2(-10d, 10d), new Vec2(20, 0)
    });

    public final Vec2[] points;

    private POINTS(Vec2[] points) {
        this.points = points;
    }

}
