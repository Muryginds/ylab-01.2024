package io.ylab.configuration.property;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class MigrationProperties {
    @Value("${spring.liquibase.changeLog}")
    private String changeLog;

    @Value("${spring.liquibase.liquibase-schema}")
    private String liquibaseSchema;

    @Value("${spring.liquibase.default-schema}")
    private String defaultSchema;
}