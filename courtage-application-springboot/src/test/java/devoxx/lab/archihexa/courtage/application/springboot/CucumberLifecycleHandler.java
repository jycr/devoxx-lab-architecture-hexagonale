package devoxx.lab.archihexa.courtage.application.springboot;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestRunStarted;
import io.restassured.RestAssured;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static io.restassured.RestAssured.when;

public class CucumberLifecycleHandler implements EventListener {
	private static WireMockServer wireMockServer;

	private static void onTestRunStarted() {
		startApiBourse();
		startCourtageApplication(getApiBourseUrl());
	}

	private static void onTestRunFinished() {
		stopCourtageApplication();
		stopApiBourse();
	}

	public static String getApiBourseUrl() {
		return "http://localhost:" + wireMockServer.port();
	}

	private static void startApiBourse() {
		wireMockServer = new WireMockServer();
		wireMockServer.start();
	}

	private static void stopApiBourse() {
		wireMockServer.stop();
	}

	private static File TEMP_DB_DIR = null;

	private static void startBdd() {
		try {
			TEMP_DB_DIR = Files.createTempDirectory("courtage-bdd-").toFile();
		} catch (IOException e) {
			throw new IllegalStateException("Impossible de créer le dossier temporaire pour la base de données", e);
		}
	}

	private static void stopBdd() {
		org.springframework.util.FileSystemUtils.deleteRecursively(TEMP_DB_DIR);
	}

	private static String getJdbcUrl() {
		return "jdbc:h2:./" + TEMP_DB_DIR.getName() + "/testDb";
	}

	private static void startCourtageApplication(String apiBourseBaseUri) {
		CourtageSpringbootApplication.main(
			"--spring.profiles.active=test",
			"--application.bourse.baseUri=" + apiBourseBaseUri
		);
		RestAssured.port = CourtageSpringbootApplication.getPort().orElseThrow();
		// Vérification du endpoint proposé par défaut
		when()
			.get("/courtage/version")
			.then()
			.statusCode(200);
	}

	private static void stopCourtageApplication() {
		CourtageSpringbootApplication.stop();
	}

	@Override
	public void setEventPublisher(EventPublisher publisher) {
		publisher.registerHandlerFor(TestRunStarted.class, event -> onTestRunStarted());
		publisher.registerHandlerFor(TestRunFinished.class, event -> onTestRunFinished());
	}
}
