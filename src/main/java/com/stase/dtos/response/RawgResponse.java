package com.stase.dtos.response;

import java.util.List;

import com.stase.dtos.game.GameRawgDto;

public record RawgResponse(
        int count,
        String next,
        String previous,
        List<GameRawgDto> results) {
}