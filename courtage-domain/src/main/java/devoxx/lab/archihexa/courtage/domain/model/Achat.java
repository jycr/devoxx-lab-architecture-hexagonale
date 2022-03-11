package devoxx.lab.archihexa.courtage.domain.model;

public class Achat {
	private final String action;
	private final int nombre;

	public Achat(String nomAction, int nombre) {
		this.action = nomAction;
		this.nombre = nombre;
	}

	public String getAction() {
		return action;
	}

	public int getNombre() {
		return nombre;
	}
}
