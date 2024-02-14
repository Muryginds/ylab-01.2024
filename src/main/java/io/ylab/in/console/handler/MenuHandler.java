package io.ylab.in.console.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.ylab.exception.BaseMonitoringServiceException;
import io.ylab.in.console.Menu;

import static java.lang.System.out;

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
                out.println(ex.getMessage());
            } catch (NullPointerException ex) {
                out.println("Invalid command. Please try again.");
            }
        }
    }
}
