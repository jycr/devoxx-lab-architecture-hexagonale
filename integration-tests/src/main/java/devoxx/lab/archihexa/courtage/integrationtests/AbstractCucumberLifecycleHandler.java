package devoxx.lab.archihexa.courtage.integrationtests;

import devoxx.lab.archihexa.courtage.integrationtests.port.secondaire.BourseMock;
import devoxx.lab.archihexa.courtage.integrationtests.port.secondaire.Db;
import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestRunStarted;
import io.restassured.RestAssured;

import static io.restassured.RestAssured.when;

public abstract class AbstractCucumberLifecycleHandler implements EventListener {
	private static final BourseMock bourseMock = BourseMock.INSTANCE;
	private static final Db db = new Db();

	private void onTestRunStarted() {
		bourseMock.start();
		db.init();
		int port = startCourtageApplication(bourseMock, db);
		RestAssured.port = port;
		// Vérification du endpoint proposé par défaut
		when()
			.get("/courtage/version")
			.then()
			.statusCode(200);
	}

	private void onTestRunFinished() {
		stopCourtageApplication();
		db.cleanup();
		bourseMock.stop();
	}

	public abstract int startCourtageApplication(BourseMock bourseMock, Db db);

	public abstract void stopCourtageApplication();

	@Override
	public void setEventPublisher(EventPublisher publisher) {
		publisher.registerHandlerFor(TestRunStarted.class, event -> onTestRunStarted());
		publisher.registerHandlerFor(TestRunFinished.class, event -> onTestRunFinished());
	}
}
