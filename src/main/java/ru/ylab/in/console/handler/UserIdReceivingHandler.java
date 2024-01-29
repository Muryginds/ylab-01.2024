package ru.ylab.in.console.handler;

import lombok.RequiredArgsConstructor;
import ru.ylab.controller.UserController;

@RequiredArgsConstructor
public class UserIdReceivingHandler extends Handler {
    private final UserController userController;

    public Long handle() {
        System.out.println("Enter user id:");
        var id = -1L;
        while (id < 0) {
            try {
                var answer = SCANNER.nextLine();
                id = Long.parseLong(answer);
                if (!userController.checkUserExistsById(id)) {
                    System.out.printf("User with id '%s' not found%n", id);
                    id = -1;
                }
            } catch (NumberFormatException ex) {
                System.out.println("Input must be number");
            }
        }
        return id;
    }
}
