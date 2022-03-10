package devoxx.lab.hexagonalarchitecture.courtage.domain.service.impl;

import devoxx.lab.hexagonalarchitecture.courtage.domain.metier.Portefeuille;
import devoxx.lab.hexagonalarchitecture.courtage.domain.service.CourtageService;

public class Courtage implements CourtageService {

	@Override
	public Portefeuille creerPortefeuille(String idPortefeuille) {
		return new Portefeuille(idPortefeuille);
	}

	@Override
	public boolean existe(String idPortefeuille) {
		return true;
	}

}
