package devoxx.lab.archihexa.courtage.application.springboot.adapters.persistence;

import devoxx.lab.archihexa.courtage.domain.model.Portefeuille;
import devoxx.lab.archihexa.courtage.domain.port.secondaire.PortefeuilleRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class PortefeuilleRepositoryInMemoryImpl implements PortefeuilleRepository {
	private final Map<String, Portefeuille> portefeuilles;

	public PortefeuilleRepositoryInMemoryImpl() {
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
