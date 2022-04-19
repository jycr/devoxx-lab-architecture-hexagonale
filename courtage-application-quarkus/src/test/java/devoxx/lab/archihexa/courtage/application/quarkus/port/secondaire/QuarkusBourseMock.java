package devoxx.lab.archihexa.courtage.application.quarkus.port.secondaire;

import devoxx.lab.archihexa.courtage.integrationtests.port.secondaire.BourseMock;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Map;

public class QuarkusBourseMock implements QuarkusTestResourceLifecycleManager {
	private final BourseMock bourseMock = BourseMock.INSTANCE;

	@Override
	public Map<String, String> start() {
		bourseMock.start();
		return Map.of(
			"quarkus.rest-client.bourse-api.url", bourseMock.getApiBourseUrl()
		);
	}

	@Override
	public void stop() {
		bourseMock.stop();
	}
}
