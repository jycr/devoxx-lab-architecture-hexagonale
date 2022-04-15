package devoxx.lab.archihexa.courtage.integrationtests;

import java.util.Map;

public class AjoutAction {
	private final String portefeuille;
	private final String action;
	private final int nombre;

	AjoutAction(String portefeuille, String action, int nombre) {
		this.portefeuille = portefeuille;
		this.action = action;
		this.nombre = nombre;
	}

	public String portefeuille() {
		return portefeuille;
	}

	public String action() {
		return action;
	}

	public int nombre() {
		return nombre;
	}

	static AjoutAction fromValues(Map<String, String> row) {
		try {
			return new AjoutAction(
				row.get("Portefeuille"),
				row.get("Action"),
				Integer.parseInt(row.get("Nombre"))
			);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
