package devoxx.lab.hexagonalarchitecture.courtage.domain.port.primaire;

import devoxx.lab.hexagonalarchitecture.courtage.domain.exception.PortefeuilleDejaExistantException;
import devoxx.lab.hexagonalarchitecture.courtage.domain.exception.PortefeuilleNonGereException;
import devoxx.lab.hexagonalarchitecture.courtage.domain.model.Portefeuille;

import java.math.BigDecimal;

public interface ServiceCourtage {
	Portefeuille creerPortefeuille(String idPortefeuille) throws PortefeuilleDejaExistantException;

	BigDecimal calculerValeurPortefeuille(String idPortefeuille) throws PortefeuilleNonGereException;

	boolean gere(String idPortefeuille);
}
