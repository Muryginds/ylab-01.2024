package ru.ylab.in.console;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.ylab.controller.*;
import ru.ylab.dto.request.SubmissionByDateRequestDTO;
import ru.ylab.in.console.handler.ConsoleInputHandler;
import ru.ylab.utils.ConsoleUtils;

import java.util.HashMap;
import java.util.Map;

import static ru.ylab.in.console.CommonUserMenu.MenuAction.*;

/**
 * Represents the menu for common user actions in the Monitoring Service console application.
 *
 * <p>This menu provides options for submitting readings, retrieving submissions, and logging out.
 */
@Slf4j
@RequiredArgsConstructor
public class CommonUserMenu extends Menu {
    private static final Map<String, MenuAction> ACTIONS = generateActions();
    private final UserController userController;
    private final LoginController loginController;
    private final SubmissionController submissionController;
    private final ReadingsRecordingController readingsRecordingController;
    private final MeterReadingsController meterReadingsController;
    private final ConsoleInputHandler consoleInputHandler;

    private static Map<String, MenuAction> generateActions() {
        Map<String, MenuAction> map = new HashMap<>();
        map.put("1", NEW_SUBMISSION);
        map.put("2", GET_LAST_SUBMISSION);
        map.put("3", GET_SUBMISSION_BY_DATE);
        map.put("4", GET_ALL_SUBMISSIONS);
        map.put("5", LOGOUT);
        return map;
    }

    public boolean executeCommand(String command) {
        return switch (ACTIONS.get(command)) {
            case NEW_SUBMISSION -> submitReadings();
            case GET_LAST_SUBMISSION -> getMyLastSubmission();
            case GET_SUBMISSION_BY_DATE -> getSubmissionByDate();
            case GET_ALL_SUBMISSIONS -> getMySubmissions();
            case LOGOUT -> logout();
            default -> throw new IllegalArgumentException("No suitable ACTION");
        };
    }

    private boolean getSubmissionByDate() {
        var userDTO = userController.getCurrentUser();
        var request = new SubmissionByDateRequestDTO(consoleInputHandler.handleDate(), userDTO.id());
        var submissionDTO = submissionController.getSubmissionByDate(request);
        var sb = new StringBuilder();
        ConsoleUtils.submissionFormattedOutput(submissionDTO, sb);
        meterReadingsController.getAllBySubmissionId(submissionDTO.id())
                .forEach(mr -> ConsoleUtils.meterReadingFormattedOutput(mr, sb));
        log.info(sb.toString());
        return false;
    }

    private boolean getMyLastSubmission() {
        var userDTO = userController.getCurrentUser();
        var submissionDTO = submissionController.getLastSubmissionByUserId(userDTO.id());
        var sb = new StringBuilder();
        ConsoleUtils.submissionFormattedOutput(submissionDTO, sb);
        meterReadingsController.getAllBySubmissionId(submissionDTO.id())
                .forEach(mr -> ConsoleUtils.meterReadingFormattedOutput(mr, sb));
        log.info(sb.toString());
        return false;
    }

    private boolean logout() {
        loginController.logout();
        return true;
    }

    private boolean getMySubmissions() {
        var userDTO = userController.getCurrentUser();
        var sb = new StringBuilder();
        submissionController.getAllByUserId(userDTO.id()).forEach(
                s -> {
                    ConsoleUtils.submissionFormattedOutput(s, sb);
                    meterReadingsController.getAllBySubmissionId(s.id()).forEach(
                            mr -> ConsoleUtils.meterReadingFormattedOutput(mr, sb)
                    );
                    sb.append("---------------------------");
                }
        );
        log.info(sb.toString());
        return false;
    }

    private boolean submitReadings() {
        readingsRecordingController.saveNewSubmission(consoleInputHandler.handleSubmission());
        return false;
    }

    @Override
    Map<String, MenuAction> getMenuOptions() {
        return ACTIONS;
    }

    @RequiredArgsConstructor
    enum MenuAction implements MenuOption {
        NEW_SUBMISSION("Submit new readings"),
        GET_LAST_SUBMISSION("Get my last submission"),
        GET_SUBMISSION_BY_DATE("Get submission by date"),
        GET_ALL_SUBMISSIONS("Get all my submissions"),
        LOGOUT("Logout");

        private final String optionName;

        @Override
        public String getOptionName() {
            return optionName;
        }
    }
}
