package devoxx.lab.archihexa.courtage.application.springboot.controller;

import devoxx.lab.archihexa.courtage.domain.exception.PortefeuilleDejaExistantException;
import devoxx.lab.archihexa.courtage.domain.exception.PortefeuilleNonGereException;
import devoxx.lab.archihexa.courtage.domain.model.Achat;
import devoxx.lab.archihexa.courtage.domain.port.primaire.ServiceCourtage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
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

	@PostMapping("/portefeuilles/{nomPortefeuille}/actions")
	public ResponseEntity<String> ajoutActionsDansPortefeuille(
		@PathVariable(value = "nomPortefeuille") String nomPortefeuille,
		@Valid @RequestBody Achat achat
	) {
		try {
			serviceCourtage.ajouteAction(nomPortefeuille, achat);
		} catch (PortefeuilleNonGereException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Portefeuille non géré");
		}
		return ResponseEntity.ok().build();
	}

	@GetMapping("/portefeuilles/{nomPortefeuille}")
	public ResponseEntity<Void> portefeuilleExiste(@PathVariable(value = "nomPortefeuille") String nomPortefeuille) {
		if (serviceCourtage.gere(nomPortefeuille)) {
			return ResponseEntity.ok().build();
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/portefeuilles/{nomPortefeuille}/valorisation")
	public ResponseEntity<String> calculValorisationPortefeuille(@PathVariable(value = "nomPortefeuille") String nomPortefeuille) {
		try {
			return ResponseEntity.ok(serviceCourtage.calculerValeurPortefeuille(nomPortefeuille).toString());
		} catch (PortefeuilleNonGereException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Portefeuille non géré");
		}
	}

	@GetMapping("/portefeuilles/avoirs")
	public ResponseEntity<String> valeurEnsemblePortefeuilles() {
		return ResponseEntity.ok(serviceCourtage.calculerValeurEnsemblePortefeuilles().toString());
	}

	@GetMapping(path = "/version", produces = MediaType.TEXT_PLAIN_VALUE)
	public String version() {
		return ofNullable(CourtageResource.class.getPackage().getImplementationVersion())
			.orElse("DEV");
	}
}
