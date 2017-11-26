package org.forweb.drift.entity.drift.base;


import org.forweb.drift.entity.drift.BaseObject;
import org.forweb.geometry.shapes.Point;

public abstract class AbstractBase extends BaseObject {

    public AbstractBase(double x, double y, double angle, Point[] points, int id) {
        super(x, y, angle, points, id);
    }

    @Override
    public boolean isRelativePoints() {
        return true;
    }

    @Override
    public boolean hasImpact(BaseObject baseObject) {
        return false;
    }

    @Override
    public boolean isAlive() {
        return true;
    }
}
