package ru.ylab.service;

import ru.ylab.console.handler.*;
import ru.ylab.controller.*;
import ru.ylab.repository.impl.*;
import ru.ylab.security.Password4jPasswordEncoder;

/**
 * A service class that initializes and runs the console-based application.
 * It manages various controllers, handlers, and repositories to facilitate user interactions.
 */
public class ConsoleService {
    private final InMemoryMeterTypeRepository inMemoryMeterTypeRepository = new InMemoryMeterTypeRepository();
    private final AuditionEventService auditionEventService =
            new AuditionEventService(new InMemoryAuditionEventRepository());
    private final MeterService meterService =
            new MeterService(new InMemoryMeterRepository(), inMemoryMeterTypeRepository);
    private final UserService userService = new UserService(
            new InMemoryUserRepository(),
            new Password4jPasswordEncoder(),
            auditionEventService,
            meterService);
    private final MeterTypeService meterTypeService =
            new MeterTypeService(inMemoryMeterTypeRepository, auditionEventService, userService);
    private final MeterReadingsService meterReadingsService =
            new MeterReadingsService(new InMemoryMeterReadingsRepository());
    private final SubmissionService submissionService = new SubmissionService(
            new InMemorySubmissionRepository(), meterReadingsService, meterService, userService, auditionEventService);
    private final UserController userController = new UserController(userService);
    private final SubmissionController submissionController = new SubmissionController(submissionService);
    private final MeterReadingsController meterReadingsController = new MeterReadingsController(meterReadingsService);
    private final MeterTypeController meterTypeController = new MeterTypeController(meterTypeService);
    private final MeterController meterController = new MeterController(meterService);
    private final RegistrationHandler registrationHandler = new RegistrationHandler(userController);
    private final AuthorizationHandler authorizationHandler = new AuthorizationHandler();
    private final DateReceivingHandler dateReceivingHandler = new DateReceivingHandler();
    private final SubmissionReceivingHandler submissionReceivingHandler = new SubmissionReceivingHandler(
            userController, submissionController, meterReadingsController, meterController);
    private final UserMenuHandler userMenuHandler = new UserMenuHandler(
            userController,
            submissionController,
            meterReadingsController,
            submissionReceivingHandler,
            dateReceivingHandler,
            meterTypeController,
            new MeterTypeReceivingHandler(meterTypeController),
            new UserIdReceivingHandler(userController),
            new AuditionEventController(auditionEventService)
    );

    /**
     * The main method that initializes controllers, handlers, and repositories,
     * and orchestrates the flow of the console-based application.
     */
    public void run() {
        var entranceMenuHandler =
                new EntranceMenuHandler(
                        userController,
                        registrationHandler,
                        authorizationHandler,
                        userMenuHandler
                );
        entranceMenuHandler.handleMenu();
    }
}
