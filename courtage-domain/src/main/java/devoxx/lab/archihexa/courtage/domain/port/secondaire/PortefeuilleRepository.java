package devoxx.lab.archihexa.courtage.domain.port.secondaire;

import devoxx.lab.archihexa.courtage.domain.model.Portefeuille;

import java.util.Optional;

public interface PortefeuilleRepository {
	boolean existe(String nomPortefeuille);

	void sauvegarde(Portefeuille portefeuille);

	Optional<Portefeuille> recupere(String id);
}
