package io.ylab.exception;

public class NoSubmissionException extends BaseMonitoringServiceException {

    public NoSubmissionException(String username) {
        super(String.format("No submissions found for user '%s'", username));
    }

    public NoSubmissionException(Long submissionId) {
        super(String.format("No submissions found with id '%s'", submissionId));
    }
}
