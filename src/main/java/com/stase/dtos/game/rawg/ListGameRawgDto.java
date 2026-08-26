package com.stase.dtos.game.rawg;

import java.util.List;

public record ListGameRawgDto(Long id, String name, String description,
        List<GenreRawgDto> genres, Boolean isPhysical) {

}
