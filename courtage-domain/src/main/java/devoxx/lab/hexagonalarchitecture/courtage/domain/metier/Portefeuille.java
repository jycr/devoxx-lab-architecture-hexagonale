package devoxx.lab.hexagonalarchitecture.courtage.domain.metier;

public class Portefeuille {

	private final String id;

	public Portefeuille(String idPortefeuille) {
		this.id = idPortefeuille;
	}

	public String getId() {
		return id;
	}
}
