package ru.ylab.in.console;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.ylab.controller.*;
import ru.ylab.dto.request.AuditionEventsRequestDTO;
import ru.ylab.dto.request.NewMeterTypeRequestDTO;
import ru.ylab.dto.response.SubmissionDTO;
import ru.ylab.dto.request.AllSubmissionsRequestDTO;
import ru.ylab.dto.request.SubmissionRequestDTO;
import ru.ylab.in.console.handler.ConsoleInputHandler;
import ru.ylab.utils.ConsoleUtils;

import java.util.HashMap;
import java.util.Map;

import static java.lang.System.*;
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
    private final LoginController loginController;
    private final SubmissionController submissionController;
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
        var request = AllSubmissionsRequestDTO.builder().userId(userId).build();
        var sb = new StringBuilder();
        submissionController.getAllSubmissionDTOs(request).forEach(
                s -> {
                    sb.append(prepareSubmissionInfoOutput(s));
                    sb.append("---------------------------");
                }
        );
        out.println(sb);
        return false;
    }

    private boolean getAuditionHistoryByUserId() {
        var userId = consoleInputHandler.handleUserId();
        var request = AuditionEventsRequestDTO.builder().userId(userId).build();
        var events = auditionEventController.getEvents(request);
        events.forEach(e -> out.printf(
                "User: #'%s' event: '%s' date: '%s message: '%s'%n",
                e.userDTO().id(), e.eventType().name(), e.date(), e.message()));
        return false;
    }

    private boolean getSubmissionsByUserIdAndDate() {
        var userId = consoleInputHandler.handleUserId();
        var date = consoleInputHandler.handleDate();
        var request = new SubmissionRequestDTO(date, userId);
        var submissionDTO = submissionController.getSubmissionDTO(request);
        var outputString = prepareSubmissionInfoOutput(submissionDTO).toString();
        out.println(outputString);
        return false;
    }

    private boolean submitMeterType() {
        var meterTypeName = consoleInputHandler.handleMeterType();
        var request = NewMeterTypeRequestDTO.builder().typeName(meterTypeName).build();
        meterTypeController.save(request);
        return false;
    }

    private boolean getLastSubmissionByUserId() {
        var userId = consoleInputHandler.handleUserId();
        var request = SubmissionRequestDTO.builder().userId(userId).build();
        var submissionDTO = submissionController.getSubmissionDTO(request);
        var outputString = prepareSubmissionInfoOutput(submissionDTO).toString();
        out.println(outputString);
        return false;
    }

    private boolean logout() {
        loginController.logout();
        return true;
    }

    private StringBuilder prepareSubmissionInfoOutput(SubmissionDTO submissionDTO) {
        var sb = new StringBuilder();
        ConsoleUtils.submissionFormattedOutput(submissionDTO, sb);
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
