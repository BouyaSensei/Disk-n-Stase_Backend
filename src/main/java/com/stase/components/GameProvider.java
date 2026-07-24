package com.stase.components;

import java.util.List;

import com.stase.dtos.game.GameDto;

public interface GameProvider {
    List<GameDto> fetchAllGames();

    boolean supports();

}
