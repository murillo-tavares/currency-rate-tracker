package br.com.currencyratetracker.currency_rate_tracker_api.infrastructure.scheduler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Habilitado por padrão; desligado em testes via {@code scheduling.enabled=false},
 * pra jobs agendados não competirem com os testes pelos mesmos beans mockados.
 */
@Configuration
@ConditionalOnProperty(name = "scheduling.enabled", havingValue = "true", matchIfMissing = true)
@EnableScheduling
class SchedulingConfig {
}
