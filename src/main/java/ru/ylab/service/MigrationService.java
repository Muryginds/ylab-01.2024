package ru.ylab.service;

import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.command.core.helpers.DbUrlConnectionArgumentsCommandStep;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import ru.ylab.exception.MigrationException;
import ru.ylab.utils.DbConnectionUtils;
import ru.ylab.utils.PropertiesUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static ru.ylab.utils.PropertiesUtils.PropertiesType.MIGRATIONS;

public class MigrationService {
    private static final Properties migrationProperties = PropertiesUtils.getProperties(MIGRATIONS);

    public void updateMigrations() {

        var changelogFilePath = migrationProperties.getProperty("changeLogFile");

        try (var connection = DbConnectionUtils.getConnection();
             var serviceSchemaCreatingPreparedStatement =
                     connection.prepareStatement("CREATE SCHEMA IF NOT EXISTS service");
             var privateSchemaCreatingPreparedStatement =
                     connection.prepareStatement("CREATE SCHEMA IF NOT EXISTS private")
        ) {
            serviceSchemaCreatingPreparedStatement.execute();
            privateSchemaCreatingPreparedStatement.execute();

            CommandScope updateCommand =
                    new CommandScope(UpdateCommandStep.COMMAND_NAME)
                            .addArgumentValue(UpdateCommandStep.CHANGELOG_FILE_ARG, changelogFilePath)
                            .addArgumentValue(DbUrlConnectionArgumentsCommandStep.DATABASE_ARG, getDatabase(connection));
            updateCommand.execute();
        } catch (LiquibaseException | SQLException e) {
            throw new MigrationException(e);
        }
    }

    private Database getDatabase(Connection connection) throws DatabaseException {
        var updateSchema = migrationProperties.getProperty("liquibaseSchemaName");
        var defaultSchema = migrationProperties.getProperty("defaultSchemaName");

        var database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(connection));
        database.setDefaultSchemaName(defaultSchema);
        database.setLiquibaseSchemaName(updateSchema);

        return database;
    }
}
