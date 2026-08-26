package com.stase.components;

import java.util.List;

import com.stase.dtos.game.ListGameRawgDto;

public interface GameProvider {
    List<ListGameRawgDto> fetchAllGames();

    boolean supports();

}
