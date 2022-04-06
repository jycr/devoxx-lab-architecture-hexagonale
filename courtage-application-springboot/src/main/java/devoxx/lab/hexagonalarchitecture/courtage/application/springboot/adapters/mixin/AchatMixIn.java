package devoxx.lab.hexagonalarchitecture.courtage.application.springboot.adapters.mixin;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AchatMixIn {

	AchatMixIn(@JsonProperty("action") String action, @JsonProperty("nombre") int nombre) {
	}
}
