package devoxx.lab.hexagonalarchitecture.courtage.domain.service.impl;

import devoxx.lab.hexagonalarchitecture.courtage.domain.exception.PortefeuilleDejaExistantException;
import devoxx.lab.hexagonalarchitecture.courtage.domain.exception.PortefeuilleNonGereException;
import devoxx.lab.hexagonalarchitecture.courtage.domain.metier.Portefeuille;
import devoxx.lab.hexagonalarchitecture.courtage.domain.port.secondaire.PortefeuilleRepository;
import devoxx.lab.hexagonalarchitecture.courtage.domain.service.CourtageService;

import java.math.BigDecimal;

public class Courtage implements CourtageService {

	private final PortefeuilleRepository repo;

	public Courtage(PortefeuilleRepository repo) {
		this.repo = repo;
	}

	@Override
	public Portefeuille creerPortefeuille(String idPortefeuille) {
		if (repo.existe(idPortefeuille)) {
			throw new PortefeuilleDejaExistantException();
		}
		Portefeuille portefeuille = new Portefeuille(idPortefeuille);
		return repo.ajouter(portefeuille);
	}

	@Override
	public boolean existe(String idPortefeuille) {
		return repo.existe(idPortefeuille);
	}

	@Override
	public BigDecimal valeurPortefeuille(String idPortefeuille) {
		if (!repo.existe(idPortefeuille)) {
			throw new PortefeuilleNonGereException();
		}
		return BigDecimal.ZERO;
	}

}
