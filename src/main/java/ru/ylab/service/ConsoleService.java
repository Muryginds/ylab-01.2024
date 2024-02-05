package ru.ylab.service;

import ru.ylab.controller.*;
import ru.ylab.in.console.handler.ConsoleInputHandler;
import ru.ylab.in.console.handler.MenuHandlerFactory;
import ru.ylab.repository.MeterTypeRepository;
import ru.ylab.repository.impl.*;
import ru.ylab.security.Password4jPasswordEncoder;
import ru.ylab.utils.DbConnectionFactory;
import ru.ylab.utils.ProdDbConnectionFactory;

/**
 * A service class that initializes and runs the console-based application.
 * It manages various controllers, handlers, and repositories to facilitate user interactions.
 */
public class ConsoleService {
    private final DbConnectionFactory dbConnectionFactory = new ProdDbConnectionFactory();
    private final MeterTypeRepository meterTypeRepository = new JdbcMeterTypeRepository(dbConnectionFactory);
    private final UserService userService = new UserService(new JdbcUserRepository(dbConnectionFactory));
    private final AuditionEventService auditionEventService =
            new AuditionEventService(new JdbcAuditionEventRepository(dbConnectionFactory), userService);
    private final MeterTypeService meterTypeService =
            new MeterTypeService(meterTypeRepository, auditionEventService, userService);
    private final MeterService meterService =
            new MeterService(new JdbcMeterRepository(dbConnectionFactory), meterTypeService, userService);
    private final LoginService loginService = new LoginService(
            userService,
            new Password4jPasswordEncoder(),
            auditionEventService,
            meterService
    );
    private final SubmissionService submissionService = new SubmissionService(
            new JdbcSubmissionRepository(dbConnectionFactory), userService, auditionEventService);
    private final MeterReadingsService meterReadingsService =
            new MeterReadingsService(new JdbcMeterReadingsRepository(dbConnectionFactory), submissionService, meterService);
    private final ReadingsRecordingService readingsRecordingService = new ReadingsRecordingService(
            meterReadingsService, meterService, submissionService, userService, auditionEventService);
    private final LoginController loginController = new LoginController(loginService);
    private final UserController userController = new UserController(userService);
    private final SubmissionController submissionController = new SubmissionController(submissionService);
    private final ReadingsRecordingController readingsRecordingController =
            new ReadingsRecordingController(readingsRecordingService);
    private final MeterReadingsController meterReadingsController = new MeterReadingsController(meterReadingsService);
    private final MeterTypeController meterTypeController = new MeterTypeController(meterTypeService);
    private final MeterController meterController = new MeterController(meterService);
    private final AuditionEventController auditionEventController = new AuditionEventController(auditionEventService);
    private final ConsoleInputHandler consoleInputHandler = new ConsoleInputHandler(
            userController, meterTypeController, submissionController, meterReadingsController, meterController);
    private final MenuHandlerFactory menuHandlerFactory = new MenuHandlerFactory(
            userController,
            loginController,
            submissionController,
            readingsRecordingController,
            meterReadingsController,
            auditionEventController,
            meterTypeController,
            consoleInputHandler
    );

    /**
     * The main method that initializes controllers, handlers, and repositories,
     * and orchestrates the flow of the console-based application.
     */
    public void run() {
        var menuHandler = menuHandlerFactory.getMenuHandler();
        menuHandler.handleMenu();
    }
}
