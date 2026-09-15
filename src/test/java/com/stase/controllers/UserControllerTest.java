package com.stase.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.stase.dtos.librairy.LibrairyDto;
import com.stase.dtos.user.UserCreateDto;
import com.stase.dtos.user.UserDto;
import com.stase.services.UserService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    private UserController userController;

    @BeforeEach
    void setUp() {
        // Crée le contrôleur avec un service mocké avant chaque test
        userController = new UserController(userService);
    }

    @Test
    void getAllUserTest() {
        // Arrange — on prépare les données attendues
        UserDto user1 = new UserDto(
            1L,
            "John",
            "john.doe@example.com",
            new LibrairyDto(1L)
        );
        UserDto user2 = new UserDto(
            2L,
            "Jane",
            "jane.smith@example.com",
            new LibrairyDto(2L)
        );
        List<UserDto> expectedUsers = Arrays.asList(user1, user2);

        // Le service mocké retourne la liste de users quand on l'appelle
        when(userService.getAllUsers()).thenReturn(expectedUsers);

        // Act — on appelle la méthode du contrôleur à tester
        List<UserDto> actualUsers = userController.getAllUsers();

        // Assert — on vérifie que le résultat est conforme aux attentes
        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void getUserTest() {
        UserDto user1 = new UserDto(
            1L,
            "John",
            "john.doe@example.com",
            new LibrairyDto(1L)
        );
        when(userService.getUser(1L)).thenReturn(user1);

        UserDto user = userController.getUser(1L);
        assertEquals(user1, user);
    }

    @Test
    void createUserTest() {}
}
