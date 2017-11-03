package org.forweb.drift.dto;

import org.forweb.drift.dto.drift.UpdateDto;

import java.util.List;


public class PlayersDto implements UpdateDto {

    private final List<String> ships;


    public PlayersDto(List<String> ships) {
        this.ships = ships;
    }

    @Override
    public String getType() {
        return "ships";
    }

    public List<String> getShips() {
        return ships;
    }
}
