package br.com.currencyratetracker.currency_rate_tracker_api;

import org.springframework.boot.SpringApplication;

public class TestCurrencyRateTrackerApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(CurrencyRateTrackerApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
