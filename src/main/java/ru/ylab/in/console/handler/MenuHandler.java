package ru.ylab.in.console.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.ylab.exception.BaseMonitoringServiceException;
import ru.ylab.in.console.Menu;

@Slf4j
@RequiredArgsConstructor
public class MenuHandler extends Handler {

    private final Menu menu;

    public void handleMenu() {
        boolean finished = false;
        while (!finished) {
            menu.printMenuOptions();
            var answer = SCANNER.nextLine();
            try {
                finished = menu.executeCommand(answer);
            } catch (BaseMonitoringServiceException ex) {
                log.info(ex.getMessage());
            } catch (NullPointerException ex) {
                log.info("Invalid command. Please try again.");
            }
        }
    }
}
