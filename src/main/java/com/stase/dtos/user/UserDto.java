package com.stase.dtos.user;

import com.stase.dtos.librairy.LibrairyDto;

public record UserDto(String username, String email, LibrairyDto librairyId) {

}
