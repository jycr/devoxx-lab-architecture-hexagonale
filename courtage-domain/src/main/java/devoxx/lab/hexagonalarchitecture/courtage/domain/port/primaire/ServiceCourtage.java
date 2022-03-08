package devoxx.lab.hexagonalarchitecture.courtage.domain.port.primaire;

import devoxx.lab.hexagonalarchitecture.courtage.domain.exception.PortefeuilleDejaExistantException;
import devoxx.lab.hexagonalarchitecture.courtage.domain.model.Portefeuille;

public interface ServiceCourtage {
	Portefeuille creerPortefeuille(String nomPortefeuille) throws PortefeuilleDejaExistantException;

	boolean gere(String nomPortefeuille);
}
