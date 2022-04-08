package devoxx.lab.archihexa.courtage.application.springboot.adapters;

import devoxx.lab.archihexa.courtage.domain.port.secondaire.ServiceBourse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;

@Service
public class ServiceBourseHttp implements ServiceBourse {
	HashMap<String, BigDecimal> map = new HashMap<>();

	@Override
	public BigDecimal recupererCours(String action) {
		return map.get(action);
	}

	public void setCours(String action, BigDecimal valeur) {
		map.put(action, valeur);
	}
}
