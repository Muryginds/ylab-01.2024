package io.ylab.backend.configuration.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application.openapi")
public class OpenApiProperties {
    private String title;
    private String version;
    private String description;
    private Contact contact;
    private Server server;

    public record Contact(
       String name,
       String email
    ) {
    }

    public record Server(
            String url,
            String description
    ) {
    }
}
