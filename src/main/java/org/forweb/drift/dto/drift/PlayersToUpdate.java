package org.forweb.drift.dto.drift;

import org.forweb.drift.entity.drift.Player;
import org.forweb.drift.entity.drift.SpaceShip;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlayersToUpdate {

    private List<String> ships;
    public PlayersToUpdate(Set<Player> players) {
        for(Player player : players) {
            SpaceShip spaceShip = player.getSpaceShip();
            if(spaceShip.isUpdateRequire()) {
                spaceShip.setUpdateRequire(false);
                String data = String.valueOf(spaceShip.getId());
                if (spaceShip.isUpdateAcceleration()) {
                    data += "|a:" + (spaceShip.isHasAcceleration() ? "1" : "0");
                }
                if (spaceShip.isUpdateTurn()) {
                    data += "|t:" + spaceShip.getTurn();
                }
                if (spaceShip.isUpdateFire()) {
                    data += "|f:" + (spaceShip.isFireStarted() ? "1" : "0");
                }
                if (ships == null) {
                    ships = new ArrayList<>();
                }
                ships.add(data);
            }
        }
    }


    public List<String> getShips() {
        return ships;
    }

    public boolean hasPlayers() {
        return  ships != null && !ships.isEmpty();
    }

}
