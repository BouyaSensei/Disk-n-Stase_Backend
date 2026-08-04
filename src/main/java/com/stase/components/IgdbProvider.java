package com.stase.components;

import java.util.List;

import org.springframework.stereotype.Component;

import com.stase.dtos.game.GameDto;

@Component
public class IgdbProvider implements GameProvider {
    @Override
    public List<GameDto> fetchAllGames() {

    }

    @Override
    public boolean supports() {
        return true;
    }
}
