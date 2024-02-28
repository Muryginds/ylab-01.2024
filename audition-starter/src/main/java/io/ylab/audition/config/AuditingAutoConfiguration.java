package io.ylab.audition.config;

import io.ylab.audition.annotation.EnableAudition;
import io.ylab.audition.aspect.AuditableAspect;
import io.ylab.audition.repository.EventRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ConditionalOnBean(annotation = EnableAudition.class)
public class AuditingAutoConfiguration {

    @Bean
    public AuditableAspect auditableAspect(JdbcTemplate jdbcTemplate) {
        var repository = new EventRepository(jdbcTemplate);
        return new AuditableAspect(repository);
    }
}
