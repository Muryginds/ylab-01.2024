package ru.ylab.in.console.handler;

import java.time.LocalDate;

public class DateReceivingHandler extends Handler {

    public LocalDate handle() {
        System.out.println("Enter month:");
        var month = -1;
        while (month < 0) {
            try {
                var answer = SCANNER.nextLine();
                month = Integer.parseInt(answer);
                if (month > 12 || month < 1) {
                    System.out.println("Input must be between 1 and 12");
                    month = -1;
                }
            } catch (NumberFormatException ex) {
                System.out.println("Input must be number");
            }
        }
        System.out.println("Enter year:");
        var year = -1;
        while (year < 0) {
            try {
                var answer = SCANNER.nextLine();
                year = Integer.parseInt(answer);
            } catch (NumberFormatException ex) {
                System.out.println("Input must be number");
            }
        }
        return LocalDate.of(year, month, 1);
    }
}
