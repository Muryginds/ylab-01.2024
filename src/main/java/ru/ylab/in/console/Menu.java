package ru.ylab.in.console;

import java.util.Map;

public abstract class Menu {
    public final void printMenuOptions(Map<String, String> menuOptions) {
        System.out.println();
        for (var entry : menuOptions.entrySet()) {
            System.out.printf("[%s]: %s%n", entry.getKey(), entry.getValue());
        }
    }

    public abstract Map<String, String> getMenuOptions();

    public abstract boolean executeCommand(String command);
}
