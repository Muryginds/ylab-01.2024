package ru.ylab.controller;

import lombok.RequiredArgsConstructor;
import ru.ylab.in.dto.MeterReadingDTO;
import ru.ylab.service.MeterReadingsService;

import java.util.Set;

@RequiredArgsConstructor
public class MeterReadingsController {
    private final MeterReadingsService meterReadingsService;

    public Set<MeterReadingDTO> getAllBySubmissionId(Long submissionId){
        return meterReadingsService.getAllBySubmissionId(submissionId);
    }
}
