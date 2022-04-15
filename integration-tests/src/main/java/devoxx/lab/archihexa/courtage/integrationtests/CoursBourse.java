package devoxx.lab.archihexa.courtage.integrationtests;

import io.cucumber.java.DataTableType;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;

public class CoursBourse {
	private final String action;
	private final BigDecimal valeur;

	CoursBourse(String action, BigDecimal valeur) {
		this.action = action;
		this.valeur = valeur;
	}

	public String action() {
		return action;
	}

	public BigDecimal valeur() {
		return valeur;
	}

	private static final NumberFormat NF = NumberFormat.getInstance(Locale.FRENCH);

	static CoursBourse fromValues(Map<String, String> row) {
		try {
			return new CoursBourse(
				row.get("Action"),
				new BigDecimal(NF.parse(row.get("Valeur")).toString())
			);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
