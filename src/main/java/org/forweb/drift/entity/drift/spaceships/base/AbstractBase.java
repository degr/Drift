package org.forweb.drift.entity.drift.spaceships.base;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.forweb.drift.entity.drift.BaseObject;
import org.forweb.drift.entity.drift.spaceships.Controlable;
import org.forweb.drift.entity.drift.spaceships.SpaceShip;
import org.forweb.geometry.misc.Vector;
import org.forweb.geometry.services.LineService;
import org.forweb.geometry.services.PointService;
import org.forweb.geometry.shapes.Point;

import java.util.HashMap;

public abstract class AbstractBase extends Controlable {

    HashMap<Integer, LandingOperation> landingOperations;

    protected abstract Point getDetectionLandingPoint();
    protected abstract Point getLandingPoint();

    public AbstractBase(double x, double y, int id, double angle, Point[] points) {
        super(x, y, angle, points, id);
        landingOperations = new HashMap<>();
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


    @JsonIgnore
    protected boolean onLand(SpaceShip spaceShip) {
        return checkLandingDistance(spaceShip) && processLanding(spaceShip);
    }

    @JsonIgnore
    private boolean processLanding(SpaceShip spaceShip) {
        landingOperations.entrySet().removeIf(entry -> !entry.getValue().expire());

        LandingOperation operation;
        if(landingOperations.containsKey(spaceShip.getId())) {
            operation = landingOperations.get(spaceShip.getId());
        } else {
            operation = new LandingOperation();
            landingOperations.put(spaceShip.getId(), operation);
        }
        if(spaceShip.isHasAcceleration() || spaceShip.getTurn() != 0 || spaceShip.isFireStarted()) {
            operation.interrupted(true);
            return false;
        } else if(operation.interrupted() && !spaceShip.isHasAcceleration() && spaceShip.getTurn() != 0 && !spaceShip.isFireStarted()) {
            if(canBeLanded(spaceShip.getVector())) {
                operation.interrupted(false);
            }
        }
        if(operation.interrupted()) {
            return false;
        }
        Vector landingVector = operation.getLandingVector();
        if(landingVector == null) {
            Point landingPoint = getLandingPoint();
            Point p = PointService.translate(new Point(0, 0), landingPoint, this.getAngle());
            p = new Point(p.getX() + getX(), p.getY() + getY());
            landingVector = new Vector(p.getX() - spaceShip.getX(), p.getY() - spaceShip.getY());
            double length = (LineService.getDistance(0, 0, landingVector.x, landingVector.y)) * 3;
            landingVector = new Vector(landingVector.x / length, landingVector.y / length);
            operation.setLandingVector(landingVector);
        }
        if(!spaceShip.getVector().equals(landingVector)) {
            spaceShip.setVector(landingVector);
            spaceShip.setUpdateRequire(true);
            spaceShip.setUpdateAcceleration(true);
        }
        landingOperations.get(spaceShip.getId()).updateTs();
        return true;
    }

    private boolean canBeLanded(Vector vector) {
        Vector result = new Vector(
                this.getVector().x - vector.x,
                this.getVector().y - vector.y
        );
        double d = Math.sqrt(Math.pow(result.x, 2) + Math.pow(result.y, 2));
        return d < 0.5;
    }

    @JsonIgnore
    private boolean checkLandingDistance(SpaceShip spaceShip) {

        Point p = PointService.translate(
                new Point(0, 0),
                getDetectionLandingPoint(),
                this.getAngle()
        );
        double distance = LineService.getDistance(
                new Point(spaceShip.getX(), spaceShip.getY()),
                new Point(p.getX() + this.getX(), p.getY() + this.getY())
        );
        if(distance < 80 && !spaceShip.isHasAcceleration() && spaceShip.getTurn() == 0 && !spaceShip.isFireStarted()) {
            return canBeLanded(spaceShip.getVector());
        }
        return false;
    }
}
