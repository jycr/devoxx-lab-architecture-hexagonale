package devoxx.lab.hexagonalarchitecture.courtage.domain.model;

public class Achat {
	private final String action;
	private final int nombre;


	public Achat(String action, int nombre) {
		this.action = action;
		this.nombre = nombre;
	}

	public String getAction() {
		return action;
	}

	public int getNombre() {
		return nombre;
	}


}
