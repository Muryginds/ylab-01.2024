package ru.ylab.console.handler;

import lombok.RequiredArgsConstructor;
import ru.ylab.controller.UserController;
import ru.ylab.in.dto.request.UserRegistrationRequestDTO;

@RequiredArgsConstructor
public class RegistrationHandler extends Handler {
    private final UserController userController;

    public UserRegistrationRequestDTO handle() {
        System.out.println("Enter name:");
        var name = SCANNER.nextLine();
        while (userController.checkUserExistsByName(name)) {
            System.out.println("Current name is already taken. Try another one");
            name = SCANNER.nextLine();
        }
        System.out.println("Enter password:");
        var password = SCANNER.nextLine();
        while (password.isBlank()) {
            System.out.println("Password must not be blank. Try another one");
            password = SCANNER.nextLine();
        }
        return UserRegistrationRequestDTO.builder()
                .name(name)
                .password(password)
                .build();
    }
}
