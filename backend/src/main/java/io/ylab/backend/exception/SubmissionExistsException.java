package io.ylab.backend.exception;

import java.time.LocalDate;

public class SubmissionExistsException extends BaseMonitoringServiceException {

    public SubmissionExistsException(String userName, LocalDate date) {
        super(String.format(
                "Submission from user '%s' for period of '%s-%s' already exists",
                userName,
                date.getYear(),
                date.getMonthValue()
        ));
    }
}
