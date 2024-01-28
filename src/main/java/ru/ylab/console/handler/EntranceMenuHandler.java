package ru.ylab.console.handler;

import lombok.RequiredArgsConstructor;
import ru.ylab.console.EntranceMenu;
import ru.ylab.controller.UserController;

@RequiredArgsConstructor
public class EntranceMenuHandler extends MenuHandler {
    private final UserController userController;
    private final RegistrationHandler registrationHandler;
    private final AuthorizationHandler authorizationHandler;
    private final UserMenuHandler userMenuHandler;


    public void handleMenu() {
        var menu = new EntranceMenu(
                registrationHandler,
                authorizationHandler,
                userMenuHandler,
                userController
        );
        var finished = false;
        while (!finished) {
            menu.printMenuOptions(menu.getMenuOptions());
            var answer = SCANNER.nextLine();
            finished = menu.executeCommand(answer);
        }
    }
}
