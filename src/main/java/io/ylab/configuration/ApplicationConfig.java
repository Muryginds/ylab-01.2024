package io.ylab.configuration;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import java.util.Objects;

@Configuration
public class ApplicationConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer yamlPropertiesConfigurer() {
        var configurer = new PropertySourcesPlaceholderConfigurer();
        var factoryBean = new YamlPropertiesFactoryBean();
        factoryBean.setResources(new ClassPathResource("application.yaml"));
        configurer.setProperties(Objects.requireNonNull(factoryBean.getObject()));
        return configurer;
    }
}
