package devoxx.lab.hexagonalarchitecture.courtage.application.springboot.controller;

import devoxx.lab.hexagonalarchitecture.courtage.domain.exception.PortefeuilleDejaExistantException;
import devoxx.lab.hexagonalarchitecture.courtage.domain.port.primaire.ServiceCourtage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static java.util.Optional.ofNullable;

@RestController
@RequestMapping("/courtage")
public class CourtageResource {

	private final ServiceCourtage serviceCourtage;

	public CourtageResource(ServiceCourtage serviceCourtage) {
		this.serviceCourtage = serviceCourtage;
	}

	@PostMapping("/portefeuilles/{nomPortefeuille}")
	public ResponseEntity<String> creationPortefeuille(@PathVariable(value = "nomPortefeuille") String nomPortefeuille) {
		try {
			serviceCourtage.creerPortefeuille(nomPortefeuille);
		} catch (PortefeuilleDejaExistantException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Portefeuille déjà géré");
		}
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().build(nomPortefeuille);
		return ResponseEntity.created(uri).build();
	}

	@GetMapping("/portefeuilles/{nomPortefeuille}")
	public ResponseEntity<Void> portefeuilleExiste(@PathVariable(value = "nomPortefeuille") String nomPortefeuille) {
		if (serviceCourtage.gere(nomPortefeuille)) {
			return ResponseEntity.ok().build();
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	}

	@GetMapping(path = "/version", produces = MediaType.TEXT_PLAIN_VALUE)
	public String version() {
		return ofNullable(CourtageResource.class.getPackage().getImplementationVersion())
			.orElse("DEV");
	}
}
