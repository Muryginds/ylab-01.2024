package io.ylab.in.console;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static java.lang.System.*;

@Slf4j
public abstract class Menu {

    @SuppressWarnings("java:S1452")
    abstract Map<String, ? extends MenuOption> getMenuOptions();

    public void printMenuOptions() {
        for (var entry : getMenuOptions().entrySet()) {
            out.printf("[%s]: %s%n", entry.getKey(), entry.getValue().getOptionName());
        }
    }

    public abstract boolean executeCommand(String command);
}
