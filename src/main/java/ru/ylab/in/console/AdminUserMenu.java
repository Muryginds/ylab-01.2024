package ru.ylab.in.console;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.ylab.controller.*;
import ru.ylab.dto.SubmissionDTO;
import ru.ylab.dto.request.SubmissionByDateRequestDTO;
import ru.ylab.in.console.handler.ConsoleInputHandler;
import ru.ylab.utils.ConsoleUtils;

import java.util.HashMap;
import java.util.Map;

import static ru.ylab.in.console.AdminUserMenu.MenuAction.*;

/**
 * Represents the menu for administrative user actions in the Monitoring Service console application.
 *
 * <p>This menu provides options for adding a meter type, retrieving submissions, and logging out.
 */
@Slf4j
@RequiredArgsConstructor
public class AdminUserMenu extends Menu {
    private static final Map<String, MenuAction> ACTIONS = generateActions();
    private final UserController userController;
    private final SubmissionController submissionController;
    private final MeterReadingsController meterReadingsController;
    private final AuditionEventController auditionEventController;
    private final MeterTypeController meterTypeController;
    private final ConsoleInputHandler consoleInputHandler;

    private static Map<String, MenuAction> generateActions() {
        Map<String, MenuAction> map = new HashMap<>();
        map.put("1", ADD_METER_TYPE);
        map.put("2", GET_LAST_SUBMISSION_BY_USER_ID);
        map.put("3", GET_ALL_SUBMISSIONS_BY_USER_ID);
        map.put("4", GET_SUBMISSION_BY_DATE_AND_USER_ID);
        map.put("5", GET_AUDITION_HISTORY_BY_USER_ID);
        map.put("6", LOGOUT);
        return map;
    }

    public boolean executeCommand(String command) {
        return switch (ACTIONS.get(command)) {
            case ADD_METER_TYPE -> submitMeterType();
            case GET_LAST_SUBMISSION_BY_USER_ID -> getLastSubmissionByUserId();
            case GET_ALL_SUBMISSIONS_BY_USER_ID -> getAllSubmissionsByUserId();
            case GET_SUBMISSION_BY_DATE_AND_USER_ID -> getSubmissionsByUserIdAndDate();
            case GET_AUDITION_HISTORY_BY_USER_ID -> getAuditionHistoryByUserId();
            case LOGOUT -> logout();
            default -> throw new IllegalArgumentException("No suitable ACTION");
        };
    }

    private boolean getAllSubmissionsByUserId() {
        var userId = consoleInputHandler.handleUserId();
        var sb = new StringBuilder();
        submissionController.getAllByUserId(userId).forEach(
                s -> {
                    sb.append(prepareSubmissionInfoOutput(s));
                    sb.append("---------------------------");
                }
        );
        log.info(sb.toString());
        return false;
    }

    private boolean getAuditionHistoryByUserId() {
        var userId = consoleInputHandler.handleUserId();
        var events = auditionEventController.getEventsByUserId(userId);
        events.forEach(e -> log.info("User: #'{}' event: '{}' date: '{} message: '{}'",
                e.userDTO().id(), e.type().name(), e.date(), e.message()));
        return false;
    }

    private boolean getSubmissionsByUserIdAndDate() {
        var userId = consoleInputHandler.handleUserId();
        var date = consoleInputHandler.handleDate();
        var request = new SubmissionByDateRequestDTO(date, userId);
        var submissionDTO = submissionController.getSubmissionByDate(request);
        var outputString = prepareSubmissionInfoOutput(submissionDTO).toString();
        log.info(outputString);
        return false;
    }

    private boolean submitMeterType() {
        var meterTypeName = consoleInputHandler.handleMeterType();
        meterTypeController.save(meterTypeName);
        return false;
    }

    private boolean getLastSubmissionByUserId() {
        var userId = consoleInputHandler.handleUserId();
        var submissionDTO = submissionController.getLastSubmissionByUserId(userId);
        var outputString = prepareSubmissionInfoOutput(submissionDTO).toString();
        log.info(outputString);
        return false;
    }

    private boolean logout() {
        userController.logout();
        return true;
    }

    private StringBuilder prepareSubmissionInfoOutput(SubmissionDTO submissionDTO) {
        var sb = new StringBuilder();
        ConsoleUtils.submissionFormattedOutput(submissionDTO, sb);
        meterReadingsController.getAllBySubmissionId(submissionDTO.id())
                .forEach(mr -> ConsoleUtils.meterReadingFormattedOutput(mr, sb));
        return sb;
    }

    @Override
    Map<String, MenuAction> getMenuOptions() {
        return ACTIONS;
    }

    @RequiredArgsConstructor
    enum MenuAction implements MenuOption {
        ADD_METER_TYPE("Add meter type"),
        GET_LAST_SUBMISSION_BY_USER_ID("Get last submission by user id"),
        GET_SUBMISSION_BY_DATE_AND_USER_ID("Get submission by user id and date"),
        GET_ALL_SUBMISSIONS_BY_USER_ID("Get all submissions by user id"),
        GET_AUDITION_HISTORY_BY_USER_ID("Get audition history by user id"),
        LOGOUT("Logout");

        private final String optionName;

        @Override
        public String getOptionName() {
            return optionName;
        }
    }
}
