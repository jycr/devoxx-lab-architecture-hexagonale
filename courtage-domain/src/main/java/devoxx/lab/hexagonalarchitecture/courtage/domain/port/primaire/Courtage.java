package devoxx.lab.hexagonalarchitecture.courtage.domain.port.primaire;

import devoxx.lab.hexagonalarchitecture.courtage.domain.exception.PortefeuilleDejaExistantException;
import devoxx.lab.hexagonalarchitecture.courtage.domain.exception.PortefeuilleNonGereException;
import devoxx.lab.hexagonalarchitecture.courtage.domain.model.Portefeuille;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

public class Courtage implements ServiceCourtage {
	private final Map<String, Portefeuille> portefeuilles = new HashMap<>();

	public Portefeuille creerPortefeuille(String idPortefeuille) throws PortefeuilleDejaExistantException {
		if (gere(idPortefeuille)) {
			throw new PortefeuilleDejaExistantException();
		}
		Portefeuille nouveauPortefeuille = new Portefeuille(idPortefeuille);
		this.portefeuilles.put(nouveauPortefeuille.getId(), nouveauPortefeuille);
		return nouveauPortefeuille;
	}

	public boolean gere(String idPortefeuille) {
		return portefeuilles.containsKey(idPortefeuille);
	}

	public BigDecimal calculerValeurPortefeuille(String idPortefeuille) throws PortefeuilleNonGereException {
		return ofNullable(portefeuilles.get(idPortefeuille))
			.map(portefeuille -> BigDecimal.ZERO)
			.orElseThrow(PortefeuilleNonGereException::new);
	}
}
