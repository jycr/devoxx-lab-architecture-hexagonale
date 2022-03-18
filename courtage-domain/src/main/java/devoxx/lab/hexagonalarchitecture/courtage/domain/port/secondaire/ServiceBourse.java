package devoxx.lab.hexagonalarchitecture.courtage.domain.port.secondaire;

import java.math.BigDecimal;

public interface ServiceBourse {
	BigDecimal recupererCours(String action);
}
