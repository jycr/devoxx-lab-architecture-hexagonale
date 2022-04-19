package devoxx.lab.archihexa.courtage.application.quarkus;

import devoxx.lab.archihexa.courtage.application.quarkus.port.secondaire.QuarkusBourseMock;
import devoxx.lab.archihexa.courtage.application.quarkus.port.secondaire.QuarkusDbMock;
import io.quarkiverse.cucumber.CucumberOptions;
import io.quarkiverse.cucumber.CucumberQuarkusTest;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;


@CucumberOptions(
	features = {
		"classpath:devoxx/lab/archihexa/courtage/domain/Courtage.feature",
		"classpath:devoxx/lab/archihexa/courtage/integrationtests/Validation.feature"
	},
	glue = "devoxx.lab.archihexa.courtage.integrationtests"
)
@QuarkusTestResource(QuarkusDbMock.class)
@QuarkusTestResource(QuarkusBourseMock.class)
@QuarkusTest
public class RunCucumberCourtageQuarkusApplicationTest extends CucumberQuarkusTest {
/*    public static void main(String[] args) {
        runMain(MyTest.class, args);
    }
*/
}