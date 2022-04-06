package devoxx.lab.archihexa.courtage.domain.port.secondaire;

import java.math.BigDecimal;

public interface ServiceBourse {
	BigDecimal recupererCours(String action);
}
