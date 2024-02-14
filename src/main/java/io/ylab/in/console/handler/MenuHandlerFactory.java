package io.ylab.in.console.handler;

import lombok.RequiredArgsConstructor;
import io.ylab.controller.*;
import io.ylab.exception.UserNotAuthorizedException;
import io.ylab.in.console.AdminUserMenu;
import io.ylab.in.console.CommonUserMenu;
import io.ylab.in.console.EntranceMenu;
import io.ylab.in.console.Menu;

@RequiredArgsConstructor
public class MenuHandlerFactory {
    private final UserController userController;
    private final LoginController loginController;
    private final SubmissionController submissionController;
    private final ReadingsRecordingController readingsRecordingController;
    private final AuditionEventController auditionEventController;
    private final MeterTypeController meterTypeController;
    private final ConsoleInputHandler consoleInputHandler;

    public MenuHandler getMenuHandler() {
        return new MenuHandler(getCurrentUserMenu());
    }

    private Menu getCurrentUserMenu() {
        try {
            var currentUser = userController.getCurrentUser();
            return switch (currentUser.role()) {
                case USER -> new CommonUserMenu(
                        loginController,
                        submissionController,
                        readingsRecordingController,
                        consoleInputHandler
                );
                case ADMIN -> new AdminUserMenu(
                        loginController,
                        submissionController,
                        auditionEventController,
                        meterTypeController,
                        consoleInputHandler
                );
                default -> throw new IllegalArgumentException(String.format(
                        "No default Menu for role '%s'",
                        currentUser.role())
                );
            };
        } catch (UserNotAuthorizedException ex) {
            return new EntranceMenu(
                    consoleInputHandler,
                    loginController,
                    this
            );
        }
    }
}
