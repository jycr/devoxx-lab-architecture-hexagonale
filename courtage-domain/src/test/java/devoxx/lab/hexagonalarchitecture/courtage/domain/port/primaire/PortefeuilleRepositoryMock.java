package devoxx.lab.hexagonalarchitecture.courtage.domain.port.primaire;

import devoxx.lab.hexagonalarchitecture.courtage.domain.metier.Portefeuille;
import devoxx.lab.hexagonalarchitecture.courtage.domain.port.secondaire.PortefeuilleRepository;

import java.util.HashMap;
import java.util.Map;

public class PortefeuilleRepositoryMock implements PortefeuilleRepository {

	private final Map<String, Portefeuille> map = new HashMap<>();

	public Portefeuille ajouter(Portefeuille portefeuille) {
		map.put(portefeuille.getId(), portefeuille);
		return portefeuille;
	}

	@Override
	public boolean existe(String idPortefeuille) {
		return map.containsKey(idPortefeuille);
	}

}
