package br.com.currencyratetracker.currency_rate_tracker_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CurrencyRateTrackerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyRateTrackerApiApplication.class, args);
	}

}
