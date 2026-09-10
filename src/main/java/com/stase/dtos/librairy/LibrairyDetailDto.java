package com.stase.dtos.librairy;

import java.util.List;

public record LibrairyDetailDto(Long id, Long userId, List<GameLibraryDto> games) {

}
