package ru.ylab.in.console.handler;

import lombok.RequiredArgsConstructor;
import ru.ylab.in.console.AdminUserMenu;
import ru.ylab.in.console.CommonUserMenu;
import ru.ylab.in.console.EntranceMenu;
import ru.ylab.in.console.Menu;
import ru.ylab.controller.*;

@RequiredArgsConstructor
public class MenuHandlerFactory {
    private final UserController userController;
    private final LoginController loginController;
    private final SubmissionController submissionController;
    private final ReadingsRecordingController readingsRecordingController;
    private final MeterReadingsController meterReadingsController;
    private final AuditionEventController auditionEventController;
    private final MeterTypeController meterTypeController;
    private final ConsoleInputHandler consoleInputHandler;

    public MenuHandler getMenuHandler() {
        return new MenuHandler(getCurrentUserMenu());
    }

    private Menu getCurrentUserMenu() {
        var currentUser = userController.getCurrentUser();
        if (currentUser == null) {
            return new EntranceMenu(
                    consoleInputHandler,
                    loginController,
                    this
            );
        }
        return switch (currentUser.role()) {
            case USER -> new CommonUserMenu(
                    userController,
                    loginController,
                    submissionController,
                    readingsRecordingController,
                    meterReadingsController,
                    consoleInputHandler
            );
            case ADMIN -> new AdminUserMenu(
                    loginController,
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
