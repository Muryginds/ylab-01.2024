package ru.ylab.service;

import ru.ylab.in.console.handler.ConsoleInputHandler;
import ru.ylab.in.console.handler.MenuHandlerFactory;
import ru.ylab.utils.ApplicationComponentsFactory;

/**
 * A service class that initializes and runs the console-based application.
 * It manages various controllers, handlers, and repositories to facilitate user interactions.
 */
public class ConsoleService {
    private final ConsoleInputHandler consoleInputHandler = new ConsoleInputHandler(
            ApplicationComponentsFactory.getUserController(),
            ApplicationComponentsFactory.getMeterTypeController(),
            ApplicationComponentsFactory.getSubmissionController(),
            ApplicationComponentsFactory.getMeterReadingController(),
            ApplicationComponentsFactory.getMeterController()
    );
    private final MenuHandlerFactory menuHandlerFactory = new MenuHandlerFactory(
            ApplicationComponentsFactory.getUserController(),
            ApplicationComponentsFactory.getLoginController(),
            ApplicationComponentsFactory.getSubmissionController(),
            ApplicationComponentsFactory.getReadingsRecordingController(),
            ApplicationComponentsFactory.getAuditionEventController(),
            ApplicationComponentsFactory.getMeterTypeController(),
            consoleInputHandler
    );

    /**
     * The main method that initializes controllers, handlers, and repositories,
     * and orchestrates the flow of the console-based application.
     */
    public void run() {
        var menuHandler = menuHandlerFactory.getMenuHandler();
        menuHandler.handleMenu();
    }
}
