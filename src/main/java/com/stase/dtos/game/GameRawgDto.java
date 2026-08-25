package com.stase.dtos.game;

import java.sql.Array;

public record GameRawgDto(Long id, String name, String description, Array genre, Boolean isPhysical) {

}
