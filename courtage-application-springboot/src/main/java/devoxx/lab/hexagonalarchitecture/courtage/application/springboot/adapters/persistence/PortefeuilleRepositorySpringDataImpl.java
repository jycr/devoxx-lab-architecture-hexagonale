package devoxx.lab.hexagonalarchitecture.courtage.application.springboot.adapters.persistence;

import devoxx.lab.hexagonalarchitecture.courtage.domain.model.Portefeuille;
import devoxx.lab.hexagonalarchitecture.courtage.domain.port.secondaire.PortefeuilleRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Repository
public class PortefeuilleRepositorySpringDataImpl implements PortefeuilleRepository {
	private final PortefeuilleSpringDataCrudRepository repo;

	public PortefeuilleRepositorySpringDataImpl(PortefeuilleSpringDataCrudRepository repo) {
		this.repo = repo;
	}

	@Override
	public boolean existe(String nomPortefeuille) {
		return repo.existsById(nomPortefeuille);
	}

	@Override
	public void sauvegarde(Portefeuille portefeuille) {
		repo.save(portefeuille);
	}

	@Override
	public Optional<Portefeuille> recupere(String id) {
		return repo.findById(id);
	}

	@Override
	public Stream<Portefeuille> recupereTous() {
		return StreamSupport.stream(repo.findAll().spliterator(), false);
	}

	@Override
	public void purge() {
		repo.deleteAll();
	}
}
