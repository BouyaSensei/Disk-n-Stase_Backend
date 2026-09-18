package com.stase.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.stase.dtos.librairy.LibrairyDto;
import com.stase.dtos.user.UserCreateDto;
import com.stase.dtos.user.UserDto;
import com.stase.dtos.user.UserUpdateDto;
import com.stase.exception.UserAlreadyExistsException;
import com.stase.exception.UserNotFoundException;
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
    void createUserTest() {
        UserCreateDto user1 = new UserCreateDto(
            "testos1",
            "test4587",
            "test@hotmail.fr"
        );
        when(userService.createUser(user1)).thenReturn(
            new UserDto(1L, "testos1", "test@hotmail.fr", new LibrairyDto(1L))
        );
        // Il faut appeler la méthode pour la tester !
        UserDto user = userController.createUser(user1);

        assertNotEquals(null, user);

        assertEquals(1L, user.id());
        assertEquals("testos1", user.username());
        assertEquals("test@hotmail.fr", user.email());
    }

    @Test
    void updateUserTest() {
        UserDto userDto = new UserDto(
            1L,
            "testdelete",
            "test@hotmail.fr",
            new LibrairyDto(2L)
        );
        UserUpdateDto userUpdate = new UserUpdateDto(
            "testdelete",
            "test45678",
            "test@hotmail.fr"
        );
        when(userService.updateUser(userDto.id(), userUpdate)).thenReturn(
            userDto
        );

        UserDto user = userController.updateUser(1L, userUpdate);

        assertNotEquals(null, user);
        assertEquals("testdelete", user.username());
        assertEquals(1L, user.id());
        assertEquals("test@hotmail.fr", user.email());
        assertEquals(new LibrairyDto(2L), user.librairy());
    }

    @Test
    void deleteUserTest() {
        // Arrange — l'id de l'utilisateur a supprimer
        Long userId = 1L;

        // Act — on appelle la methode du controleur a tester
        userController.deleteUser(userId);

        // Assert — on verifie que le service a bien ete appele avec le bon id
        verify(userService).deleteUser(userId);
    }

    // --- Tests des cas d'echec : les exceptions du service doivent remonter telles quelles
    // (GlobalExceptionHandler les convertit ensuite en reponses HTTP 404 / 409) ---

    @Test
    void createUserAvecUsernameDejaPrisDoitLancerUserAlreadyExistsException() {
        UserCreateDto dto = new UserCreateDto(
            "John",
            "test4587",
            "john.doe@example.com"
        );
        when(userService.createUser(dto)).thenThrow(
            new UserAlreadyExistsException(
                "Le nom d'utilisateur est deja pris : John"
            )
        );

        UserAlreadyExistsException ex = assertThrows(
            UserAlreadyExistsException.class,
            () -> userController.createUser(dto)
        );
        assertEquals(
            "Le nom d'utilisateur est deja pris : John",
            ex.getMessage()
        );
    }

    @Test
    void createUserAvecEmailDejaUtiliseDoitLancerUserAlreadyExistsException() {
        UserCreateDto dto = new UserCreateDto(
            "Jane",
            "test4587",
            "jane.smith@example.com"
        );
        when(userService.createUser(dto)).thenThrow(
            new UserAlreadyExistsException(
                "L'email est deja utilise : jane.smith@example.com"
            )
        );

        assertThrows(UserAlreadyExistsException.class, () ->
            userController.createUser(dto)
        );
    }

    @Test
    void getUserIntrouvableDoitLancerUserNotFoundException() {
        when(userService.getUser(99L)).thenThrow(
            new UserNotFoundException(99L)
        );

        UserNotFoundException ex = assertThrows(
            UserNotFoundException.class,
            () -> userController.getUser(99L)
        );
        assertEquals(
            "Aucun utilisateur trouve avec l'id : 99",
            ex.getMessage()
        );
    }

    @Test
    void updateUserIntrouvableDoitLancerUserNotFoundException() {
        UserUpdateDto update = new UserUpdateDto(
            "testdelete",
            "test45678",
            "test@hotmail.fr"
        );
        when(userService.updateUser(99L, update)).thenThrow(
            new UserNotFoundException(99L)
        );

        assertThrows(UserNotFoundException.class, () ->
            userController.updateUser(99L, update)
        );
    }

    @Test
    void updateUserAvecEmailDejaUtiliseDoitLancerUserAlreadyExistsException() {
        UserUpdateDto update = new UserUpdateDto(
            null,
            null,
            "jane.smith@example.com"
        );
        when(userService.updateUser(1L, update)).thenThrow(
            new UserAlreadyExistsException(
                "L'email est deja utilise : jane.smith@example.com"
            )
        );

        assertThrows(UserAlreadyExistsException.class, () ->
            userController.updateUser(1L, update)
        );
    }

    @Test
    void deleteUserIntrouvableDoitLancerUserNotFoundException() {
        // deleteUser retourne void : on utilise doThrow pour simuler l'echec du service
        doThrow(new UserNotFoundException(99L))
            .when(userService)
            .deleteUser(99L);

        assertThrows(UserNotFoundException.class, () ->
            userController.deleteUser(99L)
        );
    }
}
