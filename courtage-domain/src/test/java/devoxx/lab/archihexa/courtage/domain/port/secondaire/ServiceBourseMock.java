package devoxx.lab.archihexa.courtage.domain.port.secondaire;

import java.math.BigDecimal;
import java.util.HashMap;

public class ServiceBourseMock implements ServiceBourse {
	HashMap<String, BigDecimal> map = new HashMap<>();

	@Override
	public BigDecimal recupererCours(String action) {
		return map.get(action);
	}

	public void setCours(String action, BigDecimal valeur) {
		map.put(action, valeur);
	}
}
