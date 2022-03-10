package devoxx.lab.hexagonalarchitecture.courtage.domain.service;

import devoxx.lab.hexagonalarchitecture.courtage.domain.metier.Portefeuille;

public interface CourtageService {

	Portefeuille creerPortefeuille(String idPortefeuille);

	boolean existe(String idPortefeuille);
}
