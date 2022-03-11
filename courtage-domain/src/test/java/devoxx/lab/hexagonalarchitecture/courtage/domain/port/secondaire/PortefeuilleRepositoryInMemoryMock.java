package devoxx.lab.hexagonalarchitecture.courtage.domain.port.secondaire;

import devoxx.lab.hexagonalarchitecture.courtage.domain.model.Portefeuille;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PortefeuilleRepositoryInMemoryMock implements PortefeuilleRepository {
	private final Map<String, Portefeuille> portefeuilles;

	public PortefeuilleRepositoryInMemoryMock() {
		portefeuilles = new HashMap<>();
	}

	@Override
	public boolean existe(String nomPortefeuille) {
		return portefeuilles.containsKey(nomPortefeuille);
	}

	@Override
	public void sauvegarde(Portefeuille portefeuille) {
		portefeuilles.put(portefeuille.getNom(), portefeuille);
	}

	@Override
	public Optional<Portefeuille> recupere(String id) {
		return Optional.ofNullable(portefeuilles.get(id));
	}

	@Override
	public Collection<Portefeuille> recupereTous() {
		return portefeuilles.values();
	}
}
