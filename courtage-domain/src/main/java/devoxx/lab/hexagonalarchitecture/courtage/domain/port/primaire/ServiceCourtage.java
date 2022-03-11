package devoxx.lab.hexagonalarchitecture.courtage.domain.port.primaire;

import devoxx.lab.hexagonalarchitecture.courtage.domain.exception.PortefeuilleDejaExistantException;
import devoxx.lab.hexagonalarchitecture.courtage.domain.exception.PortefeuilleNonGereException;
import devoxx.lab.hexagonalarchitecture.courtage.domain.model.Portefeuille;

import java.math.BigDecimal;

public interface ServiceCourtage {
	Portefeuille creerPortefeuille(String nomPortefeuille) throws PortefeuilleDejaExistantException;

	BigDecimal calculerValeurPortefeuille(String nomPortefeuille) throws PortefeuilleNonGereException;

	boolean gere(String nomPortefeuille);

	void ajouteAction(int nombreActions, String nomAction, String nomPortefeuille) throws PortefeuilleNonGereException;

	BigDecimal calculerValeurEnsemblePortefeuilles();
}
