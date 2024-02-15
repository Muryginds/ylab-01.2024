package io.ylab.utils;

import lombok.experimental.UtilityClass;
import io.ylab.dto.response.MeterReadingDto;
import io.ylab.dto.response.SubmissionDto;

@UtilityClass
public class ConsoleUtils {

    public void submissionFormattedOutput(SubmissionDto submissionDTO, StringBuilder sb) {
        sb.append("Submission by user '")
                .append(submissionDTO.userDTO().name())
                .append("' id #'")
                .append(submissionDTO.userDTO().id())
                .append("' at ")
                .append(submissionDTO.date())
                .append("\n");
        submissionDTO.readings().forEach(mr -> ConsoleUtils.meterReadingFormattedOutput(mr, sb));
    }

    public void meterReadingFormattedOutput(MeterReadingDto meterReadingDTO, StringBuilder sb) {
        sb.append("Meter #'")
                .append(meterReadingDTO.meterDTO().factoryNumber())
                .append("' type:'")
                .append(meterReadingDTO.meterDTO().meterTypeDTO().typeName())
                .append("' value:")
                .append(meterReadingDTO.value())
                .append("\n");
    }
}
