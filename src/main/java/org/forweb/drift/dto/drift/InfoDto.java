package org.forweb.drift.dto.drift;

import org.forweb.drift.entity.drift.Player;

public class InfoDto implements UpdateDto {
    private final int spaceShipId;
    private final int playerId;

    public InfoDto(Player player) {
        this.playerId = player.getId();
        this.spaceShipId = player.getSpaceShip().getId();
    }

    @Override
    public String getType() {
        return "info";
    }

    public int getSpaceShipId() {
        return spaceShipId;
    }

    public int getPlayerId() {
        return playerId;
    }
}
