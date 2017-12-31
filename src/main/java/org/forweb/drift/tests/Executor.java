package org.forweb.drift.tests;

import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;

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
            case 87:
                spaceShip.command(0, PRESSED == type ? "1" : "0");
                break;
            case 83:
                spaceShip.command(1, PRESSED == type ? "1:s" : "0");
                spaceShip.command(2, PRESSED == type ? "1:s" : "0");
                break;
            case 65:
                spaceShip.command(2, PRESSED == type ? "1" : "0");
                break;
            case 68:
                spaceShip.command(1, PRESSED == type ? "1" : "0");
                break;
            case 88:
                spaceShip.command(1, PRESSED == type ? "1" : "0");
                spaceShip.command(2, PRESSED == type ? "1" : "0");
                break;
            case 67:
                spaceShip.command(1, PRESSED == type ? "1" : "0");
                spaceShip.command(2, PRESSED == type ? "1" : "0");
                break;
        }
    }
}
