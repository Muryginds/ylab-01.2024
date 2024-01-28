package ru.ylab.controller;

import lombok.RequiredArgsConstructor;
import ru.ylab.in.dto.SubmissionDTO;
import ru.ylab.in.dto.request.SubmissionByDateRequestDTO;
import ru.ylab.in.dto.request.SubmissionRequestDTO;
import ru.ylab.service.SubmissionService;

import java.util.Collection;

@RequiredArgsConstructor
public class SubmissionController {
    private final SubmissionService submissionService;

    public void save(SubmissionRequestDTO request) {
        submissionService.save(request);
    }

    public Collection<SubmissionDTO> getAllByUserId(Long userId) {
        return submissionService.getAllByUserId(userId);
    }

    public SubmissionDTO getSubmissionByDate(SubmissionByDateRequestDTO request) {
        return submissionService.getSubmissionByDateAndUserId(request);
    }

    public SubmissionDTO getLastSubmissionByUserId(Long userId) {
        return submissionService.getLastSubmissionByUserId(userId);
    }
}
