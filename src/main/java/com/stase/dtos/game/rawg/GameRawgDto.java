package com.stase.dtos.game.rawg;

import java.util.List;

public record GameRawgDto(Long id, String name, String description,
                List<String> genres, Boolean isPhysical, String platforms, String CoverImageUrl) {

}
