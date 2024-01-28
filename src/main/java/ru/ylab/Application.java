package ru.ylab;

import ru.ylab.service.ConsoleService;

/**
 * The main application class that initializes and runs the ConsoleService.
 * This class serves as the entry point for the application.
 */
public class Application {

    /**
     * The main method that initializes a ConsoleService and runs it.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        var service = new ConsoleService();
        service.run();
    }
}
