package org.forweb.drift.dto;

import org.forweb.drift.dto.drift.UpdateDto;
import org.forweb.drift.entity.drift.BaseObject;
import org.forweb.drift.entity.drift.SpaceShip;

import java.util.List;


public class PlayersDto implements UpdateDto {

    /*private final List<SpaceShip> ghosts;*/
    private final List<String> ships;
    private final BaseObject[] newObjects;


    public PlayersDto(List<String> ships, BaseObject[] newObjects/*, List<SpaceShip> ghosts*/) {
        this.ships = ships;
        //this.ghosts = ghosts;
        if(newObjects != null) {
            this.newObjects = newObjects;
        } else {
            this.newObjects = null;
        }
    }

    @Override
    public String getType() {
        return "ships";
    }

    /*public List<SpaceShip> getGhosts() {
        return ghosts;
    }*/
    public List<String> getShips() {
        return ships;
    }

    public BaseObject[] getNewObjects() {
        return newObjects;
    }
}
