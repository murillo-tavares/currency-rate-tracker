package br.com.currencyratetracker.currency_rate_tracker_api.support.suite;

import br.com.currencyratetracker.currency_rate_tracker_api.TestcontainersConfiguration;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base para testes de integração com contexto Spring completo e banco/cache reais via Testcontainers.
 * Cada teste roda em transação própria, revertida ao final, então nenhum dado criado num teste
 * vaza para o próximo. Tag "integration" permite excluir essa suíte lenta de {@code mvn test} e
 * rodá-la à parte com {@code mvn test -Dgroups=integration}.
 */
@Tag("integration")
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@TestPropertySource(properties = {
        "scheduling.enabled=false", // não concorre com os beans mockados
        "spring.cache.type=none" // cache não participa da transação; religa por teste quando precisar
})
@Transactional
public abstract class IntegrationTest {
}
