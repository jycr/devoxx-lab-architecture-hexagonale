package devoxx.lab.archihexa.courtage.domain.port.primaire;

import devoxx.lab.archihexa.courtage.domain.model.Portefeuille;

public class Courtage implements ServiceCourtage {
	@Override
	public Portefeuille creerPortefeuille(String nomPortefeuille) {
		return new Portefeuille(nomPortefeuille);
	}

	@Override
	public boolean gere(String nomPortefeuille) {
		return true;
	}
}
