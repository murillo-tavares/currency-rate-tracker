package br.com.currencyratetracker.currency_rate_tracker_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class CurrencyRateTrackerApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
