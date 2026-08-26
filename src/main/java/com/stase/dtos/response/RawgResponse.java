package com.stase.dtos.response;

import java.util.List;

import com.stase.dtos.game.ListGameRawgDto;

public record RawgResponse(
        int count,
        String next,
        String previous,
        List<ListGameRawgDto> results) {
}