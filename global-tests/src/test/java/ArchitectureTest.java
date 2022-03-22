import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;

@AnalyzeClasses(packages = "com.mycompany.myapp")
public class ArchitectureTest {
	@Test
	public void test() {
		JavaClasses jc = new ClassFileImporter()
			.importPackages("devoxx.lab.hexagonalarchitecture");
		onionArchitecture()
			.domainModels("devoxx.lab.hexagonalarchitecture.courtage.domain.model..")
			.domainServices("devoxx.lab.hexagonalarchitecture.courtage.domain.port.primaire..")

			.applicationServices("devoxx.lab.hexagonalarchitecture.courtage.application.springboot.controller..")
			.adapter("persistence", "devoxx.lab.hexagonalarchitecture.courtage.application.springboot.adapters.persistence..")
			.adapter("rest", "devoxx.lab.hexagonalarchitecture.courtage.application.springboot.adapters.rest..")
			.check(jc);

	}
}