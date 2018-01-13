package org.forweb.drift.tests;

import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;
import org.forweb.geometry.misc.Vector;


import static org.forweb.drift.tests.TestsKeyListener.KEY_TYPED.PRESSED;

public class Executor implements Listener{

    private final PolygonalSpaceShip spaceShip;

    public Executor(PolygonalSpaceShip spaceShip) {
        this.spaceShip = spaceShip;
    }


    @Override
    public void listen(int keyCode, TestsKeyListener.KEY_TYPED type) {
        if(type == TestsKeyListener.KEY_TYPED.TYPED) {
            return;
        }
        switch (keyCode) {
            case 84://T
                spaceShip.command(0, PRESSED == type ? "1:0:1:-1:0" : "0");
                break;
            case 89://Y
                spaceShip.command(1, PRESSED == type ? "1:0:1:-1:0" : "0");
                break;
            case 85://U
                spaceShip.command(2, PRESSED == type ? "1:0:1:-1:0" : "0");
                break;
            case 90://Z
                spaceShip.command(0, PRESSED == type ? "1:0:0:0:0" : "0");
                spaceShip.command(1, PRESSED == type ? "1:0:0:0:0" : "0");
                spaceShip.command(2, PRESSED == type ? "1:0:0:0:0" : "0");
                break;
            case 87://W
                spaceShip.command(0, PRESSED == type ? "1:0:0:0.3:0" : "0");
                break;
            case 83://S
                spaceShip.command(1, PRESSED == type ? "1:1" : "0");
                spaceShip.command(2, PRESSED == type ? "1:1" : "0");
                break;
            case 65:
                spaceShip.command(2, PRESSED == type ? "1:0:0:-1:-0.002" : "0");
                break;
            case 68:
                spaceShip.command(1, PRESSED == type ? "1:0:0:-1:0.002" : "0");
                break;
            case 88:
                spaceShip.command(1, PRESSED == type ? "1" : "0");
                spaceShip.command(2, PRESSED == type ? "1" : "0");
                break;
            case 67:
                spaceShip.command(1, PRESSED == type ? "1" : "0");
                spaceShip.command(2, PRESSED == type ? "1" : "0");
                break;
            case 32://space
                spaceShip.command(6, PRESSED == type ? "1" : "0");
                spaceShip.command(7, PRESSED == type ? "1" : "0");
                break;
            case 70:
                break;
        }
    }
}
