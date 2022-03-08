package devoxx.lab.hexagonalarchitecture.courtage.domain.port.primaire;

import devoxx.lab.hexagonalarchitecture.courtage.domain.exception.PortefeuilleDejaExistantException;
import devoxx.lab.hexagonalarchitecture.courtage.domain.model.Portefeuille;

public class Courtage implements ServiceCourtage {
	public Portefeuille creerPortefeuille(String nomPortefeuille) throws PortefeuilleDejaExistantException {
		return new Portefeuille(nomPortefeuille);
	}

	public boolean gere(String nomPortefeuille) {
		return true;
	}
}
