package ru.ylab;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import ru.ylab.service.MigrationService;
import ru.ylab.utils.TestDbConnectionFactory;

public abstract class CommonContainerBasedTest {
    protected static TestDbConnectionFactory dbConnectionFactory;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16-alpine")
    );

    @BeforeAll
    protected static void startUp() {
        postgres.start();
        dbConnectionFactory = new TestDbConnectionFactory(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );
        var migration = new MigrationService(dbConnectionFactory);
        migration.updateMigrations();
    }

    @AfterAll
    protected static void dropDown() {
        postgres.stop();
    }
}
