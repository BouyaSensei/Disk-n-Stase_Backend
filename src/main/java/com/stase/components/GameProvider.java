package com.stase.components;

import com.stase.dtos.game.rawg.ListGameRawgDto;
import java.util.List;

public interface GameProvider {
    List<ListGameRawgDto> fetchAllGames(Long page);

    String gameDetail(Long id);

    boolean supports();
}
