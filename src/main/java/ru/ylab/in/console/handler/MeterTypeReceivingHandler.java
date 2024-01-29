package ru.ylab.in.console.handler;

import lombok.RequiredArgsConstructor;
import ru.ylab.controller.MeterTypeController;

@RequiredArgsConstructor
public class MeterTypeReceivingHandler extends Handler {
    private final MeterTypeController meterTypeController;

    public String handle() {
        System.out.println("Enter new meter name:");
        var name = SCANNER.nextLine();
        while (name.isBlank() || meterTypeController.checkExistsByName(name)) {
            if (name.isBlank()) {
                System.out.println("Name must not be blank");
            } else {
                System.out.println("Meter type with this name already exists");
            }
            name = SCANNER.nextLine();
        }
        return name;
    }
}
