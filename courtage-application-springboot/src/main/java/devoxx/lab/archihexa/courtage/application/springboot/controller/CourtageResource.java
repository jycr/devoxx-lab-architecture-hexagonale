package devoxx.lab.archihexa.courtage.application.springboot.controller;

import devoxx.lab.archihexa.courtage.domain.exception.PortefeuilleDejaExistantException;
import devoxx.lab.archihexa.courtage.domain.exception.PortefeuilleNonGereException;
import devoxx.lab.archihexa.courtage.domain.port.primaire.ServiceCourtage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;

import static java.util.Optional.ofNullable;

@RestController
@RequestMapping("/courtage")
public class CourtageResource {
	private final ServiceCourtage serviceCourtage;

	public CourtageResource(ServiceCourtage serviceCourtage) {
		this.serviceCourtage = serviceCourtage;
	}

	@GetMapping(path = "/version", produces = MediaType.TEXT_PLAIN_VALUE)
	public String version() {
		return ofNullable(CourtageResource.class.getPackage().getImplementationVersion())
			.orElse("DEV");
	}

	@PostMapping(path = "portefeuilles/{nom}")
	public ResponseEntity<String> creerPortefeuille(@PathVariable("nom") String nomPortefeuille) throws PortefeuilleDejaExistantException {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().build(nomPortefeuille);

		serviceCourtage.creerPortefeuille(nomPortefeuille);

		return ResponseEntity.created(uri).build();
	}

	@RequestMapping (path = "portefeuilles/{nom}", method = RequestMethod.HEAD)
		public ResponseEntity<String> portefeuilleExiste (@PathVariable("nom") String nomPortefeuille) {
		if (serviceCourtage.gere(nomPortefeuille)) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping(path = "portefeuilles/{nom}/valeur")
	public Valorisation valeurPortefeuille(@PathVariable("nom") String nomPortefeuille) throws PortefeuilleNonGereException {

		return new Valorisation(serviceCourtage.calculerValeurPortefeuille(nomPortefeuille));

	}

	@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Already exists")  // 400
	@ExceptionHandler(PortefeuilleDejaExistantException.class)
	public void conflict() {
		// Nothing to do
	}

	@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Does not exists")  // 404
	@ExceptionHandler(PortefeuilleNonGereException.class)
	public void notFound() {
		// Nothing to do
	}

	private static class Valorisation {
		private final BigDecimal valeur;

		public Valorisation(BigDecimal bigDecimal) {
			this.valeur = setMinScale(bigDecimal);
		}

		public BigDecimal getValeur() {
			return valeur;
		}
	}

	private static BigDecimal setMinScale(BigDecimal bigDecimal) {
		return bigDecimal.setScale(Math.max(1, bigDecimal.scale()));
	}
}
