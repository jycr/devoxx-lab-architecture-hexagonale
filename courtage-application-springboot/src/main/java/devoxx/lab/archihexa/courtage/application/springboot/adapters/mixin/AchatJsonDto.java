package devoxx.lab.archihexa.courtage.application.springboot.adapters.mixin;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AchatJsonDto {
	AchatJsonDto(@JsonProperty("action") String action, @JsonProperty("nombre") int nombre) {
	}
}
