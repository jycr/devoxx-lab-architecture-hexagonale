package devoxx.lab.hexagonalarchitecture.courtage.domain.model;

public class Portefeuille {
	private final String idPortefeuille;

	public Portefeuille(String idPortefeuille) {
		this.idPortefeuille = idPortefeuille;
	}

	public String getId() {
		return idPortefeuille;
	}
}
