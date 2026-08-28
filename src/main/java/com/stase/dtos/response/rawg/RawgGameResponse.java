package com.stase.dtos.response.rawg;

import com.stase.dtos.game.rawg.GameRawgDto;

public record RawgGameResponse(int count, String next, String previous, GameRawgDto results) {
}