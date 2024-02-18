package io.ylab.configuration;

import io.ylab.configuration.property.MigrationProperties;
import liquibase.integration.spring.SpringLiquibase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class LiquibaseConfig {
    private final MigrationProperties migrationProperties;
    private final DataSource dataSource;

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(migrationProperties.getChangeLog());
        liquibase.setDefaultSchema(migrationProperties.getDefaultSchema());
        liquibase.setLiquibaseSchema(migrationProperties.getLiquibaseSchema());
        return liquibase;
    }
}
