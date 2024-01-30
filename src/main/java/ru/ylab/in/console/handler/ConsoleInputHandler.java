package ru.ylab.in.console.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.ylab.controller.*;
import ru.ylab.dto.MeterReadingDTO;
import ru.ylab.dto.request.SubmissionRequestDTO;
import ru.ylab.dto.request.UserAuthorizationRequestDTO;
import ru.ylab.dto.request.UserRegistrationRequestDTO;
import ru.ylab.exception.NoSubmissionException;
import ru.ylab.exception.SubmissionExistsException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class ConsoleInputHandler extends Handler {
    public static final String INPUT_MUST_BE_NUMBER = "Input must be number";
    private final UserController userController;
    private final MeterTypeController meterTypeController;
    private final SubmissionController submissionController;
    private final MeterReadingsController meterReadingsController;
    private final MeterController meterController;

    public String handleMeterType() {
        log.info("Enter new meter name:");
        var name = SCANNER.nextLine();
        while (name.isBlank() || meterTypeController.checkExistsByName(name)) {
            if (name.isBlank()) {
                log.info("Name must not be blank");
            } else {
                log.info("Meter type with this name already exists");
            }
            name = SCANNER.nextLine();
        }
        return name;
    }

    public UserRegistrationRequestDTO handleRegistration() {
        log.info("Enter name:");
        var name = SCANNER.nextLine();
        while (userController.checkUserExistsByName(name)) {
            log.info("Current name is already taken. Try another one");
            name = SCANNER.nextLine();
        }
        log.info("Enter password:");
        var password = SCANNER.nextLine();
        while (password.isBlank()) {
            log.info("Password must not be blank. Try another one");
            password = SCANNER.nextLine();
        }
        return UserRegistrationRequestDTO.builder()
                .name(name)
                .password(password)
                .build();
    }

    public UserAuthorizationRequestDTO handleAuthorization() {
        log.info("Enter name:");
        var name = SCANNER.nextLine();
        while (name.isBlank()) {
            log.info("Username must not be blank. Please try again");
            name = SCANNER.nextLine();
        }
        log.info("Enter password:");
        var password = SCANNER.nextLine();
        while (password.isBlank()) {
            log.info("Password must not be blank. Try another one");
            password = SCANNER.nextLine();
        }
        return UserAuthorizationRequestDTO.builder()
                .name(name)
                .password(password)
                .build();
    }

    public LocalDate handleDate() {
        log.info("Enter month:");
        var month = -1;
        while (month < 0) {
            try {
                var answer = SCANNER.nextLine();
                month = Integer.parseInt(answer);
                if (month > 12 || month < 1) {
                    log.info("Input must be between 1 and 12");
                    month = -1;
                }
            } catch (NumberFormatException ex) {
                log.info(INPUT_MUST_BE_NUMBER);
            }
        }
        log.info("Enter year:");
        var year = -1;
        while (year < 0) {
            try {
                var answer = SCANNER.nextLine();
                year = Integer.parseInt(answer);
            } catch (NumberFormatException ex) {
                log.info(INPUT_MUST_BE_NUMBER);
            }
        }
        return LocalDate.of(year, month, 1);
    }

    public Long handleUserId() {
        log.info("Enter user id:");
        var id = -1L;
        while (id < 0) {
            try {
                var answer = SCANNER.nextLine();
                id = Long.parseLong(answer);
                if (!userController.checkUserExistsById(id)) {
                    log.info("User with id '{}' not found", id);
                    id = -1;
                }
            } catch (NumberFormatException ex) {
                log.info(INPUT_MUST_BE_NUMBER);
            }
        }
        return id;
    }

    public SubmissionRequestDTO handleSubmission() {
        var userDTO = userController.getCurrentUser();
        Set<MeterReadingDTO> meterReadingDTOs;
        try {
            var submissionDTO = submissionController.getLastSubmissionByUserId(userDTO.id());
            var submissionDate = submissionDTO.date().withDayOfMonth(1);
            if (!submissionDate.isBefore(LocalDate.now().withDayOfMonth(1))) {
                throw new SubmissionExistsException(userDTO.name(), LocalDate.now());
            }
            meterReadingDTOs = meterReadingsController.getAllBySubmissionId(submissionDTO.id());
        } catch (NoSubmissionException ex) {
            meterReadingDTOs = meterController.getAllByUserId(userDTO.id()).stream()
                    .map(m -> MeterReadingDTO.builder()
                            .meterDTO(m)
                            .value(0L)
                            .build())
                    .collect(Collectors.toSet());
        }
        var newReadings = new HashMap<Long, Long>();
        for (var readingDTO : meterReadingDTOs) {
            log.info(
                    "Enter meter value for '{}' meter #'{}'",
                    readingDTO.meterDTO().typeDTO().typeName(),
                    readingDTO.meterDTO().factoryNumber()
            );
            var value = -1L;
            while (value < readingDTO.value()) {
                try {
                    var answer = SCANNER.nextLine();
                    value = Long.parseLong(answer);
                    if (value < readingDTO.value()) {
                        log.info(
                                "Meter value must not be less then previous. Last value:'{}'",
                                readingDTO.value()
                        );
                    } else {
                        newReadings.put(readingDTO.meterDTO().id(), value);
                    }
                } catch (NumberFormatException ex) {
                    log.info(INPUT_MUST_BE_NUMBER);
                }
            }
        }
        return SubmissionRequestDTO.builder()
                .meterReadings(newReadings)
                .build();
    }
}
