package devoxx.lab.hexagonalarchitecture.courtage.domain.service;

import devoxx.lab.hexagonalarchitecture.courtage.domain.metier.Portefeuille;

import java.math.BigDecimal;

public interface CourtageService {

	Portefeuille creerPortefeuille(String idPortefeuille);

	boolean existe(String idPortefeuille);

	BigDecimal valeurPortefeuille(String idPortefeuille);
}
