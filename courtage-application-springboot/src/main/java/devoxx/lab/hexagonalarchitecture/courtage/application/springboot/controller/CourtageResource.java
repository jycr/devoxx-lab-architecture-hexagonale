package devoxx.lab.hexagonalarchitecture.courtage.application.springboot.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Optional.ofNullable;

@RestController
@RequestMapping("/courtage")
public class CourtageResource {
	@GetMapping(path = "/version", produces = MediaType.TEXT_PLAIN_VALUE)
	public String version() {
		return ofNullable(CourtageResource.class.getPackage().getImplementationVersion())
			.orElse("DEV");
	}
}
