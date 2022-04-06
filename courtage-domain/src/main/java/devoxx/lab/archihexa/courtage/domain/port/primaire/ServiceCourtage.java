package devoxx.lab.archihexa.courtage.domain.port.primaire;

import devoxx.lab.archihexa.courtage.domain.exception.PortefeuilleDejaExistantException;
import devoxx.lab.archihexa.courtage.domain.exception.PortefeuilleNonGereException;
import devoxx.lab.archihexa.courtage.domain.model.Achat;
import devoxx.lab.archihexa.courtage.domain.model.Portefeuille;

import java.math.BigDecimal;

public interface ServiceCourtage {
	Portefeuille creerPortefeuille(String nomPortefeuille) throws PortefeuilleDejaExistantException;

	BigDecimal calculerValeurPortefeuille(String nomPortefeuille) throws PortefeuilleNonGereException;

	boolean gere(String nomPortefeuille);

	void ajouteAction(String nomPortefeuille, Achat achat) throws PortefeuilleNonGereException;
}
