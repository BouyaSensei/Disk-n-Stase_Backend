package com.stase.dtos.librairy;

import java.util.List;

public record GameLibraryDto(Long id, String name, String description, List<String> genres,
        Boolean isPhysical, String coverImageUrl, String platforms) {

}
