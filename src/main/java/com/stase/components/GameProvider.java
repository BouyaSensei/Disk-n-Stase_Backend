package com.stase.components;

import java.util.List;

import com.stase.dtos.game.rawg.ListGameRawgDto;

public interface GameProvider {
    List<ListGameRawgDto> fetchAllGames();

    String gameDetail(Long id);

    boolean supports();

}
