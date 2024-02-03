package ru.ylab.in.console;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.ylab.controller.LoginController;
import ru.ylab.exception.BaseMonitoringServiceException;
import ru.ylab.in.console.handler.ConsoleInputHandler;
import ru.ylab.in.console.handler.MenuHandlerFactory;

import java.util.HashMap;
import java.util.Map;

import static ru.ylab.in.console.EntranceMenu.MenuAction.*;

/**
 * Represents the entrance menu of the Monitoring Service console application.
 *
 * <p>This menu provides options for user authorization, registration, and exiting the application.
 */
@Slf4j
@RequiredArgsConstructor
public class EntranceMenu extends Menu {
    private static final Map<String, MenuAction> ACTIONS = generateActions();
    private final ConsoleInputHandler consoleInputHandler;
    private final LoginController loginController;
    private final MenuHandlerFactory menuHandlerFactory;

    private static Map<String, MenuAction> generateActions() {
        Map<String, MenuAction> map = new HashMap<>();
        map.put("1", AUTHORIZATION);
        map.put("2", REGISTRATION);
        map.put("3", EXIT);
        return map;
    }

    public boolean executeCommand(String command) {
        try {
            return switch (ACTIONS.get(command)) {
                case AUTHORIZATION -> authorizeUser();
                case REGISTRATION -> registerUser();
                case EXIT -> exitApplication();
                default -> throw new IllegalArgumentException("No suitable ACTION");
            };
        } catch (BaseMonitoringServiceException ex) {
            log.info(ex.getMessage());
        } catch (NullPointerException ex) {
            log.info("Invalid command. Please try again.");
        }
        return false;
    }

    private boolean exitApplication() {
        log.info("Exiting the Monitoring Service. Goodbye!");
        return true;
    }

    private boolean registerUser() {
        var registrationRequest = consoleInputHandler.handleRegistration();
        var userDTO = loginController.register(registrationRequest);
        log.info(String.format(
                "You are registered as '%s' with id '%s', please authorize",
                userDTO.name(),
                userDTO.id()
        ));
        return false;
    }

    private boolean authorizeUser() {
        var authorizationRequest = consoleInputHandler.handleAuthorization();
        var userDTO = loginController.authorize(authorizationRequest);
        log.info(String.format("You are successfully authorized as '%s'", userDTO.name()));
        menuHandlerFactory.getMenuHandler().handleMenu();
        return false;
    }

    @Override
    Map<String, MenuAction> getMenuOptions() {
        return ACTIONS;
    }

    @RequiredArgsConstructor
    enum MenuAction implements MenuOption {
        AUTHORIZATION("Authorization"),
        REGISTRATION("Registration"),
        EXIT("Exit");

        private final String optionName;

        @Override
        public String getOptionName() {
            return optionName;
        }
    }
}