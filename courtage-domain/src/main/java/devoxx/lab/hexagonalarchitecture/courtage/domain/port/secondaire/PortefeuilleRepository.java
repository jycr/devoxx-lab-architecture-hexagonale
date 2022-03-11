package devoxx.lab.hexagonalarchitecture.courtage.domain.port.secondaire;

import devoxx.lab.hexagonalarchitecture.courtage.domain.model.Portefeuille;

import java.util.Optional;

public interface PortefeuilleRepository {
	boolean existe(String nomPortefeuille);

	void sauvegarde(Portefeuille portefeuille);

	Optional<Portefeuille> recupere(String id);
}
