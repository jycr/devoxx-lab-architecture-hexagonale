package devoxx.lab.archihexa.courtage.application.quarkus.port.secondaire;

import devoxx.lab.archihexa.courtage.integrationtests.port.secondaire.Db;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Map;

public class QuarkusDbMock extends Db implements QuarkusTestResourceLifecycleManager {
	@Override
	public Map<String, String> start() {
		super.init();
		return Map.of(
			"quarkus.datasource.jdbc.url", super.getJdbcUrl(),
			"quarkus.datasource.jdbc.driver", super.getDriverClassName(),
			"quarkus.datasource.username", super.getUsername(),
			"quarkus.datasource.password", super.getPassword()
		);
	}

	@Override
	public void stop() {
		super.cleanup();
	}
}
