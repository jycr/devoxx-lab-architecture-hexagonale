package devoxx.lab.archihexa.courtage.application.springboot.adapters;

import devoxx.lab.archihexa.courtage.domain.model.Portefeuille;
import devoxx.lab.archihexa.courtage.domain.port.secondaire.PortefeuilleRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public class PortefeuilleRepositoryDB implements PortefeuilleRepository {


	private final JpaPortefeuilleRepository portefeuilles;

	public PortefeuilleRepositoryDB(JpaPortefeuilleRepository portefeuilles) {

		this.portefeuilles = portefeuilles;
	}

	@Override
	public boolean existe(String nomPortefeuille) {
		return portefeuilles.existsById(nomPortefeuille);
	}

	@Override
	public void sauvegarde(Portefeuille portefeuille) {
		portefeuilles.save(portefeuille);
	}

	@Override
	public Optional<Portefeuille> recupere(String id) {
		return portefeuilles.findById(id);
	}

	@Override
	public Collection<Portefeuille> recupereTous() {
		return portefeuilles.findAll();
	}
}
