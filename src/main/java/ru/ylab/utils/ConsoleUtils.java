package ru.ylab.utils;

import lombok.experimental.UtilityClass;
import ru.ylab.dto.MeterReadingDTO;
import ru.ylab.dto.SubmissionDTO;

@UtilityClass
public class ConsoleUtils {

    public void submissionFormattedOutput(SubmissionDTO submissionDTO, StringBuilder sb) {
        sb.append("Submission by user '")
                .append(submissionDTO.userDTO().name())
                .append("' id #'")
                .append(submissionDTO.userDTO().id())
                .append("' at ")
                .append(submissionDTO.date())
                .append("\n");
    }

    public void meterReadingFormattedOutput(MeterReadingDTO meterReadingDTO, StringBuilder sb) {
        sb.append("Meter #'")
                .append(meterReadingDTO.meterDTO().factoryNumber())
                .append("' type:'")
                .append(meterReadingDTO.meterDTO().meterTypeDTO().typeName())
                .append("' value:")
                .append(meterReadingDTO.value())
                .append("\n");
    }
}
