package devoxx.lab.archihexa.courtage.domain.port.primaire;

import devoxx.lab.archihexa.courtage.domain.model.Portefeuille;

public interface ServiceCourtage {
	Portefeuille creerPortefeuille(String nomPortefeuille);

	boolean gere(String nomPortefeuille);
}
