package ru.ylab;

import ru.ylab.service.ConsoleService;

public class Application {
    public static void main(String[] args) {
        var service = new ConsoleService();
        service.run();
    }
}
