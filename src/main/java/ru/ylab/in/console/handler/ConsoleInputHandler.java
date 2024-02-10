package ru.ylab.in.console.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.ylab.controller.*;
import ru.ylab.dto.response.MeterReadingDTO;
import ru.ylab.dto.request.*;
import ru.ylab.exception.NoSubmissionException;
import ru.ylab.exception.SubmissionExistsException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.*;

@Slf4j
@RequiredArgsConstructor
public class ConsoleInputHandler extends Handler {
    public static final String INPUT_MUST_BE_NUMBER = "Input must be number";

    private final UserController userController;
    private final MeterTypeController meterTypeController;
    private final SubmissionController submissionController;
    private final MeterReadingController meterReadingController;
    private final MeterController meterController;

    public String handleMeterType() {
        out.println("Enter new meter name:");
        var name = SCANNER.nextLine();
        while (name.isBlank() || meterTypeController.checkExistsByName(name)) {
            if (name.isBlank()) {
                out.println("Name must not be blank");
            } else {
                out.println("Meter type with this name already exists");
            }
            name = SCANNER.nextLine();
        }
        return name;
    }

    public UserRegistrationRequestDTO handleRegistration() {
        out.println("Enter name:");
        var name = SCANNER.nextLine();
        while (userController.checkUserExistsByName(name)) {
            out.println("Current name is already taken. Try another one");
            name = SCANNER.nextLine();
        }
        out.println("Enter password:");
        var password = SCANNER.nextLine();
        while (password.isBlank()) {
            out.println("Password must not be blank. Try another one");
            password = SCANNER.nextLine();
        }
        return UserRegistrationRequestDTO.builder()
                .name(name)
                .password(password)
                .build();
    }

    public UserAuthorizationRequestDTO handleAuthorization() {
        out.println("Enter name:");
        var name = SCANNER.nextLine();
        while (name.isBlank()) {
            out.println("Username must not be blank. Please try again");
            name = SCANNER.nextLine();
        }
        out.println("Enter password:");
        var password = SCANNER.nextLine();
        while (password.isBlank()) {
            out.println("Password must not be blank. Try another one");
            password = SCANNER.nextLine();
        }
        return UserAuthorizationRequestDTO.builder()
                .name(name)
                .password(password)
                .build();
    }

    public LocalDate handleDate() {
        out.println("Enter month:");
        var month = -1;
        while (month < 0) {
            try {
                var answer = SCANNER.nextLine();
                month = Integer.parseInt(answer);
                if (month > 12 || month < 1) {
                    out.println("Input must be between 1 and 12");
                    month = -1;
                }
            } catch (NumberFormatException ex) {
                out.println(INPUT_MUST_BE_NUMBER);
            }
        }
        out.println("Enter year:");
        var year = -1;
        while (year < 0) {
            try {
                var answer = SCANNER.nextLine();
                year = Integer.parseInt(answer);
            } catch (NumberFormatException ex) {
                out.println(INPUT_MUST_BE_NUMBER);
            }
        }
        return LocalDate.of(year, month, 1);
    }

    public Long handleUserId() {
        out.println("Enter user id:");
        var id = -1L;
        while (id < 0) {
            try {
                var answer = SCANNER.nextLine();
                id = Long.parseLong(answer);
                if (!userController.checkUserExistsById(id)) {
                    out.printf("User with id '%s' not found%n", id);
                    id = -1;
                }
            } catch (NumberFormatException ex) {
                out.println(INPUT_MUST_BE_NUMBER);
            }
        }
        return id;
    }

    public NewReadingsSubmissionRequestDTO handleSubmission() {
        var userDTO = userController.getCurrentUser();
        Set<MeterReadingDTO> meterReadingDTOs;
        try {
            var request = SubmissionRequestDTO.builder().userId(userDTO.id()).build();
            var submissionDTO = submissionController.getSubmissionDTO(request);
            var date = LocalDate.parse(submissionDTO.date());
            var submissionDate = date.withDayOfMonth(1);
            if (!submissionDate.isBefore(LocalDate.now().withDayOfMonth(1))) {
                throw new SubmissionExistsException(userDTO.name(), LocalDate.now());
            }
            meterReadingDTOs = meterReadingController.getAllBySubmissionId(submissionDTO.id());
        } catch (NoSubmissionException ex) {
            meterReadingDTOs = meterController.getAllByUserId(userDTO.id()).stream()
                    .map(m -> MeterReadingDTO.builder()
                            .meterDTO(m)
                            .value(0L)
                            .build())
                    .collect(Collectors.toSet());
        }
        var newReadings = new ArrayList<ReadingRequestDTO>();
        for (var readingDTO : meterReadingDTOs) {
            out.printf(
                    "Enter meter value for '%s' meter #'%s'%n",
                    readingDTO.meterDTO().meterTypeDTO().typeName(),
                    readingDTO.meterDTO().factoryNumber()
            );
            var value = -1L;
            while (value < readingDTO.value()) {
                try {
                    var answer = SCANNER.nextLine();
                    value = Long.parseLong(answer);
                    if (value < readingDTO.value()) {
                        out.printf(
                                "Meter value must not be less then previous. Last value:'%s'%n",
                                readingDTO.value()
                        );
                    } else {
                        newReadings.add(
                                ReadingRequestDTO.builder().meterId(readingDTO.meterDTO().id()).value(value).build()
                        );
                    }
                } catch (NumberFormatException ex) {
                    out.println(INPUT_MUST_BE_NUMBER);
                }
            }
        }
        return NewReadingsSubmissionRequestDTO.builder()
                .meterReadings(newReadings)
                .build();
    }
}
