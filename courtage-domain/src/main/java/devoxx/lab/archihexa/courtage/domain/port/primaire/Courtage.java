package devoxx.lab.archihexa.courtage.domain.port.primaire;

import devoxx.lab.archihexa.courtage.domain.exception.PortefeuilleDejaExistantException;
import devoxx.lab.archihexa.courtage.domain.exception.PortefeuilleNonGereException;
import devoxx.lab.archihexa.courtage.domain.model.Portefeuille;
import devoxx.lab.archihexa.courtage.domain.port.secondaire.PortefeuilleRepository;

import java.math.BigDecimal;

public class Courtage implements ServiceCourtage {
	private final PortefeuilleRepository portefeuilleRepository;

	public Courtage(PortefeuilleRepository portefeuilleRepository) {
		this.portefeuilleRepository = portefeuilleRepository;
	}

	@Override
	public Portefeuille creerPortefeuille(String nomPortefeuille) throws PortefeuilleDejaExistantException {
		if (gere(nomPortefeuille)) {
			throw new PortefeuilleDejaExistantException();
		}
		Portefeuille nouveauPortefeuille = new Portefeuille(nomPortefeuille);
		this.portefeuilleRepository.sauvegarde(nouveauPortefeuille);
		return nouveauPortefeuille;
	}

	@Override
	public boolean gere(String nomPortefeuille) {
		return portefeuilleRepository.existe(nomPortefeuille);
	}

	@Override
	public BigDecimal calculerValeurPortefeuille(String nomPortefeuille) throws PortefeuilleNonGereException {
		if (!gere(nomPortefeuille)) {
			throw new PortefeuilleNonGereException();
		}
		return BigDecimal.ZERO;
	}
}
