package org.forweb.drift.utils;

import org.jbox2d.collision.RayCastInput;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import java.util.LinkedList;
import java.util.List;

public class DriftWorld extends World {
    public List<RayWrapper> rays;
    public DriftWorld() {
        super(new Vec2(0, 0));
        rays = new LinkedList<>();

    }

    public void addRay(RayCastInput ray) {
        rays.add(new RayWrapper(ray));
    }

    public class RayWrapper {
        public int iteration;
        public final RayCastInput ray;

        public RayWrapper(RayCastInput ray) {
            this.ray = ray;
            this.iteration = 0;
        }

    }

}
