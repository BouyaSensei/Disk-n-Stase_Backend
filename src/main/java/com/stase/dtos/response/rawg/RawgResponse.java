package com.stase.dtos.response.rawg;

import java.util.List;

import com.stase.dtos.game.rawg.ListGameRawgDto;

public record RawgResponse(
                int count,
                String next,
                String previous,
                List<ListGameRawgDto> results) {
}