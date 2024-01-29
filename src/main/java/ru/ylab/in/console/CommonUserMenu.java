package ru.ylab.in.console;

import lombok.RequiredArgsConstructor;
import ru.ylab.in.console.handler.DateReceivingHandler;
import ru.ylab.in.console.handler.SubmissionReceivingHandler;
import ru.ylab.controller.MeterReadingsController;
import ru.ylab.controller.SubmissionController;
import ru.ylab.controller.UserController;
import ru.ylab.exception.BaseMonitoringServiceException;
import ru.ylab.dto.MeterReadingDTO;
import ru.ylab.dto.SubmissionDTO;
import ru.ylab.dto.request.SubmissionByDateRequestDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the menu for common user actions in the Monitoring Service console application.
 *
 * <p>This menu provides options for submitting readings, retrieving submissions, and logging out.
 */
@RequiredArgsConstructor
public class CommonUserMenu extends Menu {
    private static final Map<String, String> ACTIONS = generateActions();
    private final UserController userController;
    private final SubmissionController submissionController;
    private final MeterReadingsController meterReadingsController;
    private final SubmissionReceivingHandler submissionReceivingHandler;
    private final DateReceivingHandler dateReceivingHandler;

    private static Map<String, String> generateActions() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "Submit readings");
        map.put("2", "Get my last submission");
        map.put("3", "Get submission by date");
        map.put("4", "Get all my submissions");
        map.put("5", "Logout");
        return map;
    }

    public boolean executeCommand(String command) {
        var action = ACTIONS.get(command);
        if (action == null) {
            action = "unknown";
        }

        return switch (action) {
            case "Submit readings" -> {
                submitReadings();
                yield false;
            }
            case "Get my last submission" -> {
                getMyLastSubmission();
                yield false;
            }
            case "Get all my submissions" -> {
                getMySubmissions();
                yield false;
            }
            case "Get submission by date" -> {
                getSubmissionByDate();
                yield false;
            }
            case "Logout" -> {
                logout();
                yield true;
            }
            default -> {
                System.out.println("Invalid command. Please try again.");
                yield false;
            }
        };
    }

    private void getSubmissionByDate() {
        try {
            var userDTO = userController.getCurrentUser();
            var request = new SubmissionByDateRequestDTO(dateReceivingHandler.handle(), userDTO.id());
            var submissionDTO = submissionController.getSubmissionByDate(request);
            var sb = new StringBuilder();
            submissionFormattedOutput(submissionDTO, sb);
            meterReadingsController.getAllBySubmissionId(submissionDTO.id())
                    .forEach(mr -> meterReadingFormattedOutput(mr, sb));
            System.out.println(sb);
        } catch (BaseMonitoringServiceException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void getMyLastSubmission() {
        try {
            var userDTO = userController.getCurrentUser();
            var submissionDTO = submissionController.getLastSubmissionByUserId(userDTO.id());
            var sb = new StringBuilder();
            submissionFormattedOutput(submissionDTO, sb);
            meterReadingsController.getAllBySubmissionId(submissionDTO.id())
                    .forEach(mr -> meterReadingFormattedOutput(mr, sb));
            System.out.println(sb);
        } catch (BaseMonitoringServiceException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void logout() {
        userController.logout();
    }

    private void getMySubmissions() {
        var userDTO = userController.getCurrentUser();
        var sb = new StringBuilder();
        submissionController.getAllByUserId(userDTO.id()).forEach(
                s -> {
                    submissionFormattedOutput(s, sb);
                    meterReadingsController.getAllBySubmissionId(s.id()).forEach(
                            mr -> meterReadingFormattedOutput(mr, sb)
                    );
                    sb.append("---------------------------");
                }
        );
        System.out.println(sb);
    }

    private void submitReadings() {
        try {
            submissionController.save(submissionReceivingHandler.handle());
        } catch (BaseMonitoringServiceException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void submissionFormattedOutput(SubmissionDTO submissionDTO, StringBuilder sb) {
        sb.append("Submission by user id #'")
                .append(submissionDTO.userDTO().id())
                .append("' at ")
                .append(submissionDTO.date())
                .append("\n");
    }

    private void meterReadingFormattedOutput(MeterReadingDTO meterReadingDTO, StringBuilder sb) {
        sb.append("Meter #'")
                .append(meterReadingDTO.meterDTO().factoryNumber())
                .append("' type:'")
                .append(meterReadingDTO.meterDTO().typeDTO().typeName())
                .append("' value:")
                .append(meterReadingDTO.value())
                .append("\n");
    }

    @Override
    public Map<String, String> getMenuOptions() {
        return ACTIONS;
    }
}
