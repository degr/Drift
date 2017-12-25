package org.forweb.drift.entity.drift.inventory;

import org.forweb.drift.entity.drift.PolygonalObject;
import org.forweb.drift.entity.drift.PolygonalObjectEntity;

import java.awt.*;

public class Inventory extends PolygonalObject{
    private double bulk;

    public Inventory(PolygonalObjectEntity configuration, double bulk) {
        super(configuration);
        this.bulk = bulk;
    }

    public double getBulk() {
        return bulk;
    }
}