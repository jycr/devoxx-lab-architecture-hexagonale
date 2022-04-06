package devoxx.lab.archihexa.courtage.application.springboot.adapters.rest;

import devoxx.lab.archihexa.courtage.domain.port.secondaire.ServiceBourse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static java.util.Optional.ofNullable;

@Service
public class ServiceBourseHttpAdapter implements ServiceBourse {

	private final RestTemplate restTemplate;
	private final String bourseUri;

	public ServiceBourseHttpAdapter(@Value("${application.bourse.baseUri}") String bourseUri) {
		this.bourseUri = bourseUri;
		this.restTemplate = new RestTemplate();
	}

	@Override
	public BigDecimal recupererCours(String action) {
		return ofNullable(
			restTemplate
				.getForEntity(bourseUri + "/finance/quote/" + action, Quote.class)
				.getBody()
		)
			.map(Quote::getRegularMarketPriceAsBigDecimal)
			.orElseThrow();
	}

	static class Quote {
		private String regularMarketPrice;

		void setRegularMarketPrice(String regularMarketPrice) {
			this.regularMarketPrice = regularMarketPrice;
		}

		BigDecimal getRegularMarketPriceAsBigDecimal() {
			return new BigDecimal(regularMarketPrice);
		}
	}
}
