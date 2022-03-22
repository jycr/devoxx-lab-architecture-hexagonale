package devoxx.lab.hexagonalarchitecture.courtage.domain.port.secondaire;

import devoxx.lab.hexagonalarchitecture.courtage.domain.model.Portefeuille;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public interface PortefeuilleRepository {
	boolean existe(String nomPortefeuille);

	void sauvegarde(Portefeuille portefeuille);

	Optional<Portefeuille> recupere(String id);

	Stream<Portefeuille> recupereTous();

	void purge();
}
