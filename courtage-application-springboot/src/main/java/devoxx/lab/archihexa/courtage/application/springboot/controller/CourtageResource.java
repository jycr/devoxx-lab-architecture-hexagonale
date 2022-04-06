package devoxx.lab.archihexa.courtage.application.springboot.controller;

import devoxx.lab.archihexa.courtage.domain.exception.PortefeuilleDejaExistantException;
import devoxx.lab.archihexa.courtage.domain.exception.PortefeuilleNonGereException;
import devoxx.lab.archihexa.courtage.domain.port.primaire.ServiceCourtage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@RestController
@RequestMapping("/courtage")
public class CourtageResource {
	private final ServiceCourtage serviceCourtage;

	public CourtageResource(ServiceCourtage serviceCourtage) {
		this.serviceCourtage = serviceCourtage;
	}

	@GetMapping("/portefeuilles/{nomPortefeuille}")
	public ResponseEntity<Void> portefeuilleExiste(@PathVariable(value = "nomPortefeuille") String nomPortefeuille) throws PortefeuilleNonGereException {
		if (!serviceCourtage.gere(nomPortefeuille)) {
			throw new PortefeuilleNonGereException();
		}
		return ResponseEntity.ok().build();
	}

	@GetMapping(path = "/version", produces = MediaType.TEXT_PLAIN_VALUE)
	public String version() {
		return ofNullable(CourtageResource.class.getPackage().getImplementationVersion()).orElse("DEV");
	}

	@PostMapping("/portefeuilles/{nomPortefeuille}")
	public ResponseEntity<String> creationPortefeuille(@PathVariable(value = "nomPortefeuille") String nomPortefeuille) throws PortefeuilleDejaExistantException {
		serviceCourtage.creerPortefeuille(nomPortefeuille);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().build(nomPortefeuille);
		return ResponseEntity.created(uri).build();
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ResponseEntity<String> handleConstraintViolationException(MethodArgumentNotValidException e) {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(e.getFieldErrors().stream()
				.map(fe -> "\t" + fe.getField() + " " + fe.getDefaultMessage())
				.collect(Collectors.joining("\n", "Donnée(s) erronée(s): \\n\"", "")));
	}

	@ExceptionHandler(PortefeuilleDejaExistantException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ResponseEntity<String> handlePortefeuilleDejaExistantException(PortefeuilleDejaExistantException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Portefeuille déjà géré");
	}

	@ExceptionHandler(PortefeuilleNonGereException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	ResponseEntity<String> handlePortefeuilleNonGereException(PortefeuilleNonGereException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Portefeuille non géré");
	}
}
