package ru.ylab.in.console.handler;

import lombok.RequiredArgsConstructor;
import ru.ylab.in.console.AdminUserMenu;
import ru.ylab.in.console.CommonUserMenu;
import ru.ylab.in.console.Menu;
import ru.ylab.controller.*;

@RequiredArgsConstructor
public class UserMenuHandlerFactory {
    private final UserController userController;
    private final SubmissionController submissionController;
    private final MeterReadingsController meterReadingsController;
    private final AuditionEventController auditionEventController;
    private final MeterTypeController meterTypeController;
    private final ConsoleInputHandler consoleInputHandler;

    public MenuHandler getCurrentUserMenuHandler() {
        return new MenuHandler(getCurrentUserMenu());
    }

    private Menu getCurrentUserMenu() {
        var currentUser = userController.getCurrentUser();
        return switch (currentUser.role()) {
            case USER -> new CommonUserMenu(
                    userController,
                    submissionController,
                    meterReadingsController,
                    consoleInputHandler
            );
            case ADMIN -> new AdminUserMenu(
                    userController,
                    submissionController,
                    meterReadingsController,
                    auditionEventController,
                    meterTypeController,
                    consoleInputHandler
            );
            default -> throw new IllegalArgumentException(String.format(
                    "No default Menu for role '%s'",
                    currentUser.role())
            );
        };
    }
}
