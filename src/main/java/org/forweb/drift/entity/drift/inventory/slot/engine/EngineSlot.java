package org.forweb.drift.entity.drift.inventory.slot.engine;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.item.Inventory;
import org.forweb.drift.entity.drift.inventory.slot.InventorySlot;
import org.forweb.drift.entity.drift.inventory.item.engine.Engine;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;
import org.forweb.drift.utils.PolygonalUtils;
import org.forweb.geometry.misc.Angle;
import org.forweb.geometry.misc.Vector;
import org.forweb.geometry.services.PointService;
import org.forweb.geometry.shapes.Point;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public abstract class EngineSlot extends InventorySlot {
    private static final int STOP_ROTATION = 1;
    private static final int CHECK_SYSTEM = 2;
    private static final int LINEAR_LIMIT = 3;
    private static final int ANGULAR_LIMIT = 4;
    private static final int CHECK_STARTED = 5;
    private static final int CHECK_FINISHED = 6;
    private static final int CHECK_DEINITIALIZED = 7;

    protected double cachedMass;
    protected double rotationAngle;
    protected double vectorLength;
    private int check;
    private double linearLimit;
    private double angularLimit;
    private boolean stopRotation;
    private Vec2 previousLinearVelocity;
    private double previousAngularVelocity;

    /**
     * if engine will be used for 100% power, it will change linear acceleration of ship for this value:
     */
    private double linearDelta;
    /**
     * if engine will be used for 100% power, it will change rotation for this value:
     */
    private double angularDelta;

    public EngineSlot(PolygonalObjectEntity configuration, PolygonalSpaceShip spaceShip) {
        super(configuration, spaceShip);
        reset();
    }

    private void reset() {
        cachedMass = 0;
        angularDelta = 0;
        linearDelta = 0;
        previousLinearVelocity = null;
        previousAngularVelocity = 0;
        check = 0;
    }


    /**
     *
     * @param command 1:1:0:10:20
     *                setActive:setStopRotation:enableSystemCheck:maxLinearVelocity:maxAngularVelocity
     */
    @Override
    public void command(String command) {
        String[] parts = command.split(":");
        boolean isActive = "1".equals(parts[0]);
        setActive(isActive);
        if(isActive) {
            if(parts.length >= STOP_ROTATION + 1) {
                stopRotation = "1".equals(parts[STOP_ROTATION]);
            } else {
                stopRotation = false;
            }
            if(parts.length >= CHECK_SYSTEM + 1 && "1".equals(parts[CHECK_SYSTEM])) {
                check = CHECK_STARTED;
            }
            if(parts.length >= LINEAR_LIMIT + 1) {
                linearLimit = Double.parseDouble(parts[LINEAR_LIMIT]);
                if(linearLimit < 0) {
                    linearLimit = -1;
                }
            } else {
                linearLimit = -1;
            }
            if(parts.length >= LINEAR_LIMIT + 1) {
                angularLimit = Double.parseDouble(parts[ANGULAR_LIMIT]);
            } else {
                angularLimit = 0;
            }
        }
    }

    @Override
    protected void applySlotConfiguration() {
        super.applySlotConfiguration();
        reset();
    }

    @Override
    public void affect(PolygonalSpaceShip spaceShip) {
        Inventory inventory = getInventory();
        if(inventory != null) {
            Body spaceShipBody = spaceShip.getBody();
            if(check != CHECK_FINISHED) {
                Engine engine = (Engine) inventory;
                double power = engine.getPower();
                if(stopRotation) {
                    double currentAngularVelocity = spaceShipBody.getAngularVelocity();
                    if(currentAngularVelocity == 0) {
                        return;
                    }
                    if(angularDelta != 0) {
                        double futureAngularVelocity = currentAngularVelocity + angularDelta;
                        if(futureAngularVelocity > 0) {
                            if(currentAngularVelocity > 0) {
                                if(currentAngularVelocity < futureAngularVelocity) {
                                    return;
                                }/* else {
                                   power = power;
                                }*/
                            } else {
                                power *= currentAngularVelocity / (currentAngularVelocity - futureAngularVelocity);
                            }
                        } else if(futureAngularVelocity < 0) {
                            if(currentAngularVelocity < 0) {
                                if(currentAngularVelocity > futureAngularVelocity) {
                                    return;
                                }/* else {
                                   power = power;
                                }*/
                            } else {
                                power *= currentAngularVelocity / (currentAngularVelocity - futureAngularVelocity);
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    double angularPower = power;
                    if (angularLimit != 0) {
                        if(angularDelta == 0) {
                            return;
                        }
                        double currentAngularVelocity = spaceShipBody.getAngularVelocity();
                        double futureAngularVelocity = currentAngularVelocity + angularDelta;
                        if(futureAngularVelocity > 0) {
                            if (angularLimit > 0 && angularLimit <= futureAngularVelocity) {
                                return;
                            }
                        } else if(futureAngularVelocity < 0) {
                            if(angularLimit < 0 && futureAngularVelocity <= angularLimit) {
                                return;
                            }
                        }
                    }
                    double linearPower = power;
                    if (linearLimit != -1 && linearLimit >= 0) {
                        if(linearDelta == 0) {
                            return;
                        }
                        double currentLinear = spaceShipBody.getLinearVelocity().length();
                        double angle = spaceShipBody.getAngle() + getAngle();
                        Vec2 iterationLinear = new Vec2(linearDelta * Math.cos(angle), linearDelta * Math.sin(angle));
                        double futureLinear = spaceShipBody.getLinearVelocity().add(iterationLinear).length();
                        if(futureLinear > linearLimit) {
                            if(currentLinear < linearLimit) {
                                double linearAcceleration = linearDelta;
                                if (linearAcceleration != 0) {
                                    linearPower *= (linearAcceleration - (futureLinear - linearLimit)) / linearAcceleration;
                                }
                                System.out.println(linearPower);
                            } else if(currentLinear <= futureLinear) {
                                return;
                            }
                        }
                    }
                    power = Math.min(linearPower, angularPower);
                }
                if(!spaceShip.useEnergy(inventory.getEnergyConsumption())) {
                    return;
                }
                double angle = getAngle() + spaceShipBody.getAngle();
                double sin = Math.sin(angle);
                double cos = Math.cos(angle);

                spaceShipBody.applyForce(
                        new Vec2(cos * power, sin * power),
                        spaceShipBody.getWorldPoint(new Vec2(getX(), getY()))
                );
            }

            if(check == CHECK_STARTED) {
                check = CHECK_FINISHED;
                previousAngularVelocity = spaceShipBody.getAngularVelocity();
                previousLinearVelocity = spaceShipBody.getLinearVelocity().clone();
            } else if(check == CHECK_FINISHED) {
                check = CHECK_DEINITIALIZED;
                linearDelta = spaceShipBody.getLinearVelocity().sub(previousLinearVelocity).length();
                angularDelta = spaceShipBody.getAngularVelocity() - previousAngularVelocity;
                setActive(false);
            }
        }
    }
}
