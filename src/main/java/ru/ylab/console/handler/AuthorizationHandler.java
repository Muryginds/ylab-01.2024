package ru.ylab.console.handler;

import ru.ylab.in.dto.request.UserAuthorizationRequestDTO;

public class AuthorizationHandler extends Handler {

    public UserAuthorizationRequestDTO handle() {
        System.out.println("Enter name:");
        var name = SCANNER.nextLine();
        while (name.isBlank()) {
            System.out.println("Username must not be blank. Please try again");
            name = SCANNER.nextLine();
        }
        System.out.println("Enter password:");
        var password = SCANNER.nextLine();
        while (password.isBlank()) {
            System.out.println("Password must not be blank. Try another one");
            password = SCANNER.nextLine();
        }
        return UserAuthorizationRequestDTO.builder()
                .name(name)
                .password(password)
                .build();
    }
}
