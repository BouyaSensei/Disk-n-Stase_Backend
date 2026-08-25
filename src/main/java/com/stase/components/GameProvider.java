package com.stase.components;

import java.util.List;

import com.stase.dtos.game.GameRawgDto;

public interface GameProvider {
    List<GameRawgDto> fetchAllGames();

    boolean supports();

}
