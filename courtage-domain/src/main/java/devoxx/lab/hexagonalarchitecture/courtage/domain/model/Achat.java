package devoxx.lab.hexagonalarchitecture.courtage.domain.model;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Achat {
	@NotNull
	@Pattern(regexp = "^[\\p{IsLatin}0-9]{2,5}$", message = "doit être composé de 2 à 5 caractères latins")
	private final String action;
	@Min(1)
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
