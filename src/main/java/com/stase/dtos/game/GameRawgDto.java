package com.stase.dtos.game;

import java.util.List;

public record GameRawgDto(Long id, String name, String description,
        List<GenreRawgDto> genres, Boolean isPhysical) {

}
