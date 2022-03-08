package devoxx.lab.hexagonalarchitecture.courtage.domain.model;

public class Portefeuille {
	private final String nom;

	public Portefeuille(String nom) {
		this.nom = nom;
	}

	public String getNom() {
		return nom;
	}
}
