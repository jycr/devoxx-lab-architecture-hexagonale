package devoxx.lab.archihexa.courtage.application.quarkus.adapters.rest;

import java.math.BigDecimal;

public class CoursBourse {
	private  String symbol;
	private  BigDecimal regularMarketPrice;

	public BigDecimal getRegularMarketPrice() {
		return regularMarketPrice;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setRegularMarketPrice(BigDecimal regularMarketPrice) {
		this.regularMarketPrice = regularMarketPrice;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
}
