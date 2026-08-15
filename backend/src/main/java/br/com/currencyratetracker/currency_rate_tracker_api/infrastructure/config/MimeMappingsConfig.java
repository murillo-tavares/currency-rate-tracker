package br.com.currencyratetracker.currency_rate_tracker_api.infrastructure.config;

import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.server.servlet.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * O servidor embarcado não mapeia {@code .yaml}/{@code .yml} por padrão.
 * {@code openapi.yaml} (servido como recurso estático) volta com {@code Content-Type: application/octet-stream},
 * o que faz o Scalar/Swagger UI falharem ao carregar a spec via {@code data-url}.
 */
@Configuration
class MimeMappingsConfig {

    @Bean
    WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> yamlMimeMappingCustomizer() {
        return factory -> {
            MimeMappings mappings = new MimeMappings();
            mappings.add("yaml", "text/yaml");
            mappings.add("yml", "text/yaml");
            factory.addMimeMappings(mappings);
        };
    }
}
