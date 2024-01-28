package ru.ylab.console.handler;

import lombok.RequiredArgsConstructor;
import ru.ylab.controller.MeterController;
import ru.ylab.controller.MeterReadingsController;
import ru.ylab.controller.SubmissionController;
import ru.ylab.controller.UserController;
import ru.ylab.exception.NoSubmissionsException;
import ru.ylab.exception.SubmissionExistsException;
import ru.ylab.in.dto.MeterReadingDTO;
import ru.ylab.in.dto.request.SubmissionRequestDTO;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SubmissionReceivingHandler extends Handler {
    private final UserController userController;
    private final SubmissionController submissionController;
    private final MeterReadingsController meterReadingsController;
    private final MeterController meterController;

    public SubmissionRequestDTO handle() {
        var userDTO = userController.getCurrentUser();
        Set<MeterReadingDTO> meterReadingDTOs;
        try {
            var submissionDTO = submissionController.getLastSubmissionByUserId(userDTO.id());
            var submissionDate = submissionDTO.date().withDayOfMonth(1);
            if (!submissionDate.isBefore(LocalDate.now().withDayOfMonth(1))) {
                throw new SubmissionExistsException(userDTO.name(), LocalDate.now());
            }
            meterReadingDTOs = meterReadingsController.getAllBySubmissionId(submissionDTO.id());
        } catch (NoSubmissionsException ex) {
            meterReadingDTOs = meterController.getAllByUserId(userDTO.id()).stream()
                    .map(m -> MeterReadingDTO.builder()
                            .meterDTO(m)
                            .value(0L)
                            .build())
                    .collect(Collectors.toSet());
        }
        var newReadings = new HashMap<Long, Long>();
        for (var readingDTO : meterReadingDTOs) {
            System.out.printf(
                    "Enter meter value for '%s' meter #'%s'%n",
                    readingDTO.meterDTO().typeDTO().typeName(),
                    readingDTO.meterDTO().factoryNumber()
            );
            var value = -1L;
            while (value < readingDTO.value()) {
                try {
                    var answer = SCANNER.nextLine();
                    value = Long.parseLong(answer);
                    if (value < readingDTO.value()) {
                        System.out.printf(
                                "Meter value must not be less then previous. Last value:'%s'%n",
                                readingDTO.value()
                        );
                    } else {
                        newReadings.put(readingDTO.meterDTO().id(), value);
                    }
                } catch (NumberFormatException ex) {
                    System.out.println("Input must be number");
                }
            }
        }
        return SubmissionRequestDTO.builder().meterReadings(newReadings).build();
    }
}
