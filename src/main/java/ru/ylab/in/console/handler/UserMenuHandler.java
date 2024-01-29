package ru.ylab.in.console.handler;

import lombok.RequiredArgsConstructor;
import ru.ylab.in.console.AdminUserMenu;
import ru.ylab.in.console.CommonUserMenu;
import ru.ylab.in.console.Menu;
import ru.ylab.controller.*;

@RequiredArgsConstructor
public class UserMenuHandler extends MenuHandler {
    private final UserController userController;
    private final SubmissionController submissionController;
    private final MeterReadingsController meterReadingsController;
    private final SubmissionReceivingHandler submissionReceivingHandler;
    private final DateReceivingHandler dateReceivingHandler;
    private final MeterTypeController meterTypeController;
    private final MeterTypeReceivingHandler meterTypeReceivingHandler;
    private final UserIdReceivingHandler userIdReceivingHandler;
    private final AuditionEventController auditionEventController;

    public void handleMenu() {
        boolean finished = false;
        var userMenu = getUserMenuHandler();
        while (!finished) {
            userMenu.printMenuOptions(userMenu.getMenuOptions());
            var answer = SCANNER.nextLine();
            finished = userMenu.executeCommand(answer);
        }
    }

    private Menu getUserMenuHandler() {
        var currentUser = userController.getCurrentUser();
        return switch (currentUser.role()) {
            case USER -> new CommonUserMenu(
                    userController,
                    submissionController,
                    meterReadingsController,
                    submissionReceivingHandler,
                    dateReceivingHandler
            );
            case ADMIN -> new AdminUserMenu(
                    userController,
                    submissionController,
                    meterReadingsController,
                    meterTypeController,
                    meterTypeReceivingHandler,
                    userIdReceivingHandler,
                    dateReceivingHandler,
                    auditionEventController
            );
            default -> throw new IllegalArgumentException(String.format(
                    "No Menu Handler for role '%s'",
                    currentUser.role())
            );
        };
    }
}
