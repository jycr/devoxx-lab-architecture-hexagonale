package devoxx.lab.archihexa.courtage.application.springboot;

import devoxx.lab.archihexa.courtage.integrationtests.AbstractCucumberLifecycleHandler;
import devoxx.lab.archihexa.courtage.integrationtests.port.secondaire.BourseMock;
import devoxx.lab.archihexa.courtage.integrationtests.port.secondaire.Db;

public class CucumberLifecycleHandler extends AbstractCucumberLifecycleHandler {
	public int startCourtageApplication(BourseMock bourseMock, Db db) {
		CourtageSpringbootApplication.main(
			"--spring.profiles.active=test",
			"--application.bourse.baseUri=" + bourseMock.getApiBourseUrl(),
			"--spring.datasource.url=" + db.getJdbcUrl(),
			"--spring.datasource.username=" + db.getUsername(),
			"--spring.datasource.password=" + db.getPassword(),
			"--spring.datasource.driverClassName=" + db.getDriverClassName()
		);
		return CourtageSpringbootApplication.getPort().orElseThrow();
	}

	public void stopCourtageApplication() {
		CourtageSpringbootApplication.stop();
	}
}
