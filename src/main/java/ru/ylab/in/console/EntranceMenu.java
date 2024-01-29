package ru.ylab.in.console;

import lombok.RequiredArgsConstructor;
import ru.ylab.controller.UserController;
import ru.ylab.exception.UserAlreadyExistException;
import ru.ylab.exception.UserAuthenticationException;
import ru.ylab.in.console.handler.AuthorizationHandler;
import ru.ylab.in.console.handler.RegistrationHandler;
import ru.ylab.in.console.handler.UserMenuHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the entrance menu of the Monitoring Service console application.
 *
 * <p>This menu provides options for user authorization, registration, and exiting the application.
 */
@RequiredArgsConstructor
public class EntranceMenu extends Menu {
    private static final Map<String, String> ACTIONS = generateActions();
    private final RegistrationHandler registrationHandler;
    private final AuthorizationHandler authorizationHandler;
    private final UserMenuHandler userMenuHandler;
    private final UserController userController;

    private static Map<String, String> generateActions() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "Authorization");
        map.put("2", "Registration");
        map.put("3", "Exit");
        return map;
    }

    public boolean executeCommand(String command) {
        var action = ACTIONS.get(command);
        if (action == null) {
            action = "unknown";
        }

        return switch (action) {
            case "Authorization" -> {
                authorizeUser();
                yield false;
            }
            case "Registration" -> {
                registerUser();
                yield false;
            }
            case "Exit" -> {
                exitApplication();
                yield true;
            }
            default -> {
                System.out.println("Invalid command. Please try again.");
                yield false;
            }
        };
    }

    private void exitApplication() {
        System.out.println("Exiting the Monitoring Service. Goodbye!");
    }

    private void registerUser() {
        try {
            var registrationRequest = registrationHandler.handle();
            var userDTO = userController.register(registrationRequest);
            System.out.printf(
                    "You are registered as '%s' with id '%s', please authorize%n",
                    userDTO.name(),
                    userDTO.id()
            );
        } catch (UserAlreadyExistException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void authorizeUser() {
        try {
            var authorizationRequest = authorizationHandler.handle();
            var userDTO = userController.authorize(authorizationRequest);
            System.out.printf("You are successfully authorized as '%s'%n", userDTO.name());
            userMenuHandler.handleMenu();
        } catch (UserAuthenticationException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public Map<String, String> getMenuOptions() {
        return ACTIONS;
    }
}
