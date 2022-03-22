package devoxx.lab.hexagonalarchitecture.courtage.domain.model;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;
import static java.util.Optional.ofNullable;

public class Portefeuille {
	private String nom;
	private final Map<String, Integer> actions = new HashMap<>();

	/**
	 * Constructeur utilisé pour le mapping JPA.
	 */
	Portefeuille() {
	}

	public Portefeuille(String nom) {
		this.nom = nom;
	}

	public String getNom() {
		return nom;
	}

	public void ajouterAction(int nombreActions, String nomAction) {
		actions.compute(
			nomAction,
			(action, ancienNbActions) -> ofNullable(ancienNbActions).orElse(0) + nombreActions
		);
	}

	public Map<String, Integer> getActions() {
		return unmodifiableMap(actions);
	}
}
