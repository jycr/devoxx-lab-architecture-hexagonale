package devoxx.lab.hexagonalarchitecture.courtage.domain.port.primaire;

import devoxx.lab.hexagonalarchitecture.courtage.domain.exception.PortefeuilleDejaExistantException;
import devoxx.lab.hexagonalarchitecture.courtage.domain.exception.PortefeuilleNonGereException;
import devoxx.lab.hexagonalarchitecture.courtage.domain.model.Portefeuille;
import devoxx.lab.hexagonalarchitecture.courtage.domain.port.secondaire.PortefeuilleRepository;

import java.math.BigDecimal;

public class Courtage implements ServiceCourtage {
	private final PortefeuilleRepository portefeuilleRepository;

	public Courtage(PortefeuilleRepository portefeuilleRepository) {
		this.portefeuilleRepository = portefeuilleRepository;
	}

	public Portefeuille creerPortefeuille(String nomPortefeuille) throws PortefeuilleDejaExistantException {
		if (gere(nomPortefeuille)) {
			throw new PortefeuilleDejaExistantException();
		}
		Portefeuille nouveauPortefeuille = new Portefeuille(nomPortefeuille);
		this.portefeuilleRepository.sauvegarde(nouveauPortefeuille);
		return nouveauPortefeuille;
	}

	public boolean gere(String nomPortefeuille) {
		return portefeuilleRepository.existe(nomPortefeuille);
	}

	public BigDecimal calculerValeurPortefeuille(String nomPortefeuille) throws PortefeuilleNonGereException {
		return portefeuilleRepository.recupere(nomPortefeuille)
			.map(portefeuille -> BigDecimal.ZERO)
			.orElseThrow(PortefeuilleNonGereException::new);
	}
}
