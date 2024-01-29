package ru.ylab.console;

import lombok.RequiredArgsConstructor;
import ru.ylab.console.handler.DateReceivingHandler;
import ru.ylab.console.handler.MeterTypeReceivingHandler;
import ru.ylab.console.handler.UserIdReceivingHandler;
import ru.ylab.controller.*;
import ru.ylab.exception.MeterTypeExistException;
import ru.ylab.exception.NoSubmissionException;
import ru.ylab.in.dto.MeterReadingDTO;
import ru.ylab.in.dto.SubmissionDTO;
import ru.ylab.in.dto.request.SubmissionByDateRequestDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the menu for administrative user actions in the Monitoring Service console application.
 *
 * <p>This menu provides options for adding a meter type, retrieving submissions, and logging out.
 */
@RequiredArgsConstructor
public class AdminUserMenu extends Menu {
    private static final Map<String, String> ACTIONS = generateActions();
    private final UserController userController;
    private final SubmissionController submissionController;
    private final MeterReadingsController meterReadingsController;
    private final MeterTypeController meterTypeController;
    private final MeterTypeReceivingHandler meterTypeReceivingHandler;
    private final UserIdReceivingHandler userIdReceivingHandler;
    private final DateReceivingHandler dateReceivingHandler;
    private final AuditionEventController auditionEventController;

    private static Map<String, String> generateActions() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "Add meter type");
        map.put("2", "Get last submission by user id");
        map.put("3", "Get all submissions by user id");
        map.put("4", "Get submission by user id and date");
        map.put("5", "Get audition history by user id");
        map.put("6", "Logout");
        return map;
    }

    public boolean executeCommand(String command) {
        var action = ACTIONS.get(command);
        if (action == null) {
            action = "unknown";
        }

        return switch (action) {
            case "Add meter type" -> {
                submitMeterType();
                yield false;
            }
            case "Get last submission by user id" -> {
                getLastSubmissionByUserId();
                yield false;
            }
            case "Get all submissions by user id" -> {
                getAllSubmissionsByUserId();
                yield false;
            }
            case "Get submission by user id and date" -> {
                getSubmissionsByUserIdAndDate();
                yield false;
            }
            case "Get audition history by user id" -> {
                getAuditionHistoryByUserId();
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

    private void getAllSubmissionsByUserId() {
        var userId = userIdReceivingHandler.handle();
        var sb = new StringBuilder();
        submissionController.getAllByUserId(userId).forEach(
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

    private void getAuditionHistoryByUserId() {
        var userId = userIdReceivingHandler.handle();
        var events = auditionEventController.getEventsByUserId(userId);
        events.forEach(e -> System.out.printf("User: #'%s' event: '%s' date: '%s message: '%s'%n",
                e.userDTO().id(), e.type().name(), e.date(), e.message()));
    }

    private void getSubmissionsByUserIdAndDate() {
        try {
            var userId = userIdReceivingHandler.handle();
            var date = dateReceivingHandler.handle();
            var request = new SubmissionByDateRequestDTO(date, userId);
            var submissionDTO = submissionController.getSubmissionByDate(request);
            var sb = new StringBuilder();
            submissionFormattedOutput(submissionDTO, sb);
            meterReadingsController.getAllBySubmissionId(submissionDTO.id())
                    .forEach(mr -> meterReadingFormattedOutput(mr, sb));
            System.out.println(sb);
        } catch (NoSubmissionException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void submitMeterType() {
        try {
            var meterTypeName = meterTypeReceivingHandler.handle();
            meterTypeController.save(meterTypeName);
        } catch (MeterTypeExistException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void getLastSubmissionByUserId() {
        try {
            var userId = userIdReceivingHandler.handle();
            var submissionDTO = submissionController.getLastSubmissionByUserId(userId);
            var sb = new StringBuilder();
            submissionFormattedOutput(submissionDTO, sb);
            meterReadingsController.getAllBySubmissionId(submissionDTO.id())
                    .forEach(mr -> meterReadingFormattedOutput(mr, sb));
            System.out.println(sb);
        } catch (NoSubmissionException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void submissionFormattedOutput(SubmissionDTO submissionDTO, StringBuilder sb) {
        sb.append("Submission at ")
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

    private void logout() {
        userController.logout();
    }

    @Override
    public Map<String, String> getMenuOptions() {
        return ACTIONS;
    }
}
