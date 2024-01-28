package ru.ylab.controller;

import lombok.RequiredArgsConstructor;
import ru.ylab.in.dto.UserDTO;
import ru.ylab.in.dto.request.UserAuthorizationRequestDTO;
import ru.ylab.in.dto.request.UserRegistrationRequestDTO;
import ru.ylab.service.UserService;

@RequiredArgsConstructor
public class UserController {
    public final UserService userService;

    public UserDTO register(UserRegistrationRequestDTO request) {
       return userService.registerUser(request);
    }

    public UserDTO authorize(UserAuthorizationRequestDTO request) {
        return userService.authorize(request);
    }

    public UserDTO getCurrentUser() {
        return userService.getCurrentUserDTO();
    }

    public boolean checkUserExistsById(Long userId) {
        return userService.checkUserExistsById(userId);
    }

    public boolean checkUserExistsByName(String name) {
        return userService.checkUserExistsByName(name);
    }

    public void logout() {
        userService.logout();
    }
}
