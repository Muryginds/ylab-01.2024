package io.ylab.backend.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER;
import static io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP;

@Configuration
public class OpenApiConfig {
    public static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().info(new Info().title("OpenApi specification - Monitoring System")
                        .version("1.0")
                        .description("OpenApi documentation for Monitoring Service")
                        .contact(new Contact().name("Muryginds")
                                .email("muryginds@gmail.com")))
                .servers(List.of(new Server().url("http://localhost:8080/monitoring-service")
                        .description("Local ENV")))
                .addSecurityItem(new SecurityRequirement()
                        .addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .description("authentication using JWT token")
                                .in(HEADER)
                                .type(HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                ;
    }
}
