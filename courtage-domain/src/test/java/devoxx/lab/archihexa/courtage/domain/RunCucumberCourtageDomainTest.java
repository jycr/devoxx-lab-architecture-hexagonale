package devoxx.lab.archihexa.courtage.domain;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.*;

@Suite
@SelectClasspathResource("devoxx/lab/archihexa/courtage")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "devoxx.lab.archihexa.courtage.domain")
@ConfigurationParameter(key = PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "true")
@ConfigurationParameter(key = PLUGIN_PUBLISH_ENABLED_PROPERTY_NAME, value = "false")
public class RunCucumberCourtageDomainTest {
}
