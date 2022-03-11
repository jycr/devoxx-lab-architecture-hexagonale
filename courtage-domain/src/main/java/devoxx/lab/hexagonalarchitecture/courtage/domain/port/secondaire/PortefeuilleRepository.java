package devoxx.lab.hexagonalarchitecture.courtage.domain.port.secondaire;

import devoxx.lab.hexagonalarchitecture.courtage.domain.metier.Portefeuille;

public interface PortefeuilleRepository {
	Portefeuille ajouter(Portefeuille portefeuille);

	boolean existe(String idPortefeuille);
}
