package br.com.currencyratetracker.currency_rate_tracker_api.infrastructure.config;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.cotacao.Cotacao;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.cotacao.Dashboard;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.service.CotacaoService;
import com.fasterxml.jackson.databind.JavaType;
import org.springframework.boot.cache.autoconfigure.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;
import java.util.List;

/**
 * Caches de cotação serializados em JSON (em vez do padrão Java nativo), pra ficar legível
 * direto no Redis, e com TTL de segurança caso o job agendado pare de rodar.
 */
@Configuration
class RedisCacheConfig {

    @Bean
    RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        JavaType tipoListaCotacao = JacksonConfig.OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, Cotacao.class);
        JavaType tipoDashboard = JacksonConfig.OBJECT_MAPPER.getTypeFactory().constructType(Dashboard.class);

        RedisCacheConfiguration configuracaoCotacoes = RedisCacheConfigUtils.json(tipoListaCotacao, Duration.ofMinutes(5));
        RedisCacheConfiguration configuracaoDashboard = RedisCacheConfigUtils.json(tipoDashboard, Duration.ofMinutes(2));

        return builder -> builder
                .withCacheConfiguration(CotacaoService.CACHE_COTACOES, configuracaoCotacoes)
                .withCacheConfiguration(CotacaoService.CACHE_DASHBOARD, configuracaoDashboard);
    }
}
