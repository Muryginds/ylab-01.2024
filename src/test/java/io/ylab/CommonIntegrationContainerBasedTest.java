package io.ylab;

import io.ylab.configuration.MainWebAppInitializer;
import io.ylab.mapper.*;
import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.command.core.helpers.DbUrlConnectionArgumentsCommandStep;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CommandExecutionException;
import liquibase.exception.DatabaseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        MainWebAppInitializer.class,
        UserMapperImpl.class,
        MeterTypeMapperImpl.class,
        MeterMapperImpl.class,
        MeterReadingMapperImpl.class,
        SubmissionMapperImpl.class
})
public abstract class CommonIntegrationContainerBasedTest {
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16-alpine")
    );

    private static JdbcTemplate jdbcTemplate;

    protected static JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @BeforeAll
    protected static void startUp() {
        postgres.start();
        var datasource = new DriverManagerDataSource();
        datasource.setUrl(postgres.getJdbcUrl());
        datasource.setUsername(postgres.getUsername());
        datasource.setPassword(postgres.getPassword());
        jdbcTemplate = new JdbcTemplate(datasource);
        updateMigrations(datasource, jdbcTemplate);
    }

    @AfterAll
    protected static void dropDown() {
        postgres.stop();
    }

    private static void updateMigrations(DataSource dataSource, JdbcTemplate jdbcTemplate) {

        var changelogFilePath = "db/changelog/changelog-master.yaml";

        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS service");
        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS private");

        try {
            CommandScope updateCommand =
                new CommandScope(UpdateCommandStep.COMMAND_NAME)
                        .addArgumentValue(UpdateCommandStep.CHANGELOG_FILE_ARG, changelogFilePath)
                        .addArgumentValue(DbUrlConnectionArgumentsCommandStep.DATABASE_ARG,
                                getDatabase(dataSource.getConnection()));
            updateCommand.execute();
        } catch (CommandExecutionException | DatabaseException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Database getDatabase(Connection connection) throws DatabaseException {
        var updateSchema = "service";
        var defaultSchema = "private";

        var database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(connection));
        database.setDefaultSchemaName(defaultSchema);
        database.setLiquibaseSchemaName(updateSchema);

        return database;
    }
}


