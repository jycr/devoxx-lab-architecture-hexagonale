package devoxx.lab.archihexa.courtage.application.springboot.controller;

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
	@GetMapping(path = "/version", produces = MediaType.TEXT_PLAIN_VALUE)
	public String version() {
		return ofNullable(CourtageResource.class.getPackage().getImplementationVersion()).orElse("DEV");
	}

	@PostMapping("/portefeuilles/{nomPortefeuille}")
	public ResponseEntity<String> creationPortefeuille(@PathVariable(value = "nomPortefeuille") String nomPortefeuille) {
		// TODO

		// Redirection vers l'URI de la ressource créé
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
}
