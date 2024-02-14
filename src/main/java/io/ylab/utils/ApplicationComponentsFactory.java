package io.ylab.utils;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import io.ylab.controller.*;
import io.ylab.repository.*;
import io.ylab.repository.impl.*;
import io.ylab.security.Password4jPasswordEncoder;
import io.ylab.security.PasswordEncoder;
import io.ylab.service.*;

@UtilityClass
public class ApplicationComponentsFactory {
    private static final DbConnectionFactory dbConnectionFactory =
            new ProdDbConnectionFactory();

    static {
        var migration = new MigrationService(dbConnectionFactory);
        migration.updateMigrations();
    }

    private static final UserRepository userRepository =
            new JdbcUserRepository(dbConnectionFactory);
    private static final SubmissionRepository submissionRepository =
            new JdbcSubmissionRepository(dbConnectionFactory);
    private static final MeterTypeRepository meterTypeRepository =
            new JdbcMeterTypeRepository(dbConnectionFactory);
    private static final MeterRepository meterRepository =
            new JdbcMeterRepository(dbConnectionFactory);
    private static final MeterReadingRepository METER_READING_REPOSITORY =
            new JdbcMeterReadingRepository(dbConnectionFactory);
    private static final AuditionEventRepository auditionEventRepository =
            new JdbcAuditionEventRepository(dbConnectionFactory);

    private static final PasswordEncoder passwordEncoder = new Password4jPasswordEncoder();
    @Getter
    private static final UserService userService =
            new UserService(userRepository);
    @Getter
    private static final AuditionEventService auditionEventService =
            new AuditionEventService(auditionEventRepository, userService);
    private static final SubmissionService submissionService =
            new SubmissionService(submissionRepository, userService);
    private static final MeterTypeService meterTypeService =
            new MeterTypeService(meterTypeRepository);
    private static final MeterService meterService =
            new MeterService(meterRepository, meterTypeService, userService);
    private static final MeterReadingsService meterReadingService =
            new MeterReadingsService(METER_READING_REPOSITORY, submissionService, meterService);
    private static final ReadingsRecordingService readingsRecordingService =
            new ReadingsRecordingService(
                    meterReadingService, meterService, submissionService, userService);
    private static final LoginService loginService =
            new LoginService(userService, passwordEncoder, meterService);
    private static final SubmissionRepresentationService submissionRepresentationService =
            new SubmissionRepresentationService(meterReadingService, submissionService);

    @Getter
    private static final UserController userController =
            new UserController(userService);
    @Getter
    private static final AuditionEventController auditionEventController =
            new AuditionEventController(auditionEventService);
    @Getter
    private static final SubmissionController submissionController =
            new SubmissionController(submissionRepresentationService);
    @Getter
    private static final MeterTypeController meterTypeController =
            new MeterTypeController(meterTypeService);
    @Getter
    private static final MeterController meterController =
            new MeterController(meterService);
    @Getter
    private static final MeterReadingController meterReadingController =
            new MeterReadingController(meterReadingService);
    @Getter
    private static final ReadingsRecordingController readingsRecordingController =
            new ReadingsRecordingController(readingsRecordingService);
    @Getter
    private static final LoginController loginController =
            new LoginController(loginService);
    @Getter
    private static final RequestValidator requestValidator =
            new RequestValidator();
}
