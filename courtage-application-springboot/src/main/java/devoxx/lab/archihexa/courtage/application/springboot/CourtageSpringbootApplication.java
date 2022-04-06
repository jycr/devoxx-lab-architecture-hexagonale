package devoxx.lab.archihexa.courtage.application.springboot;

import devoxx.lab.archihexa.courtage.domain.DomainService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.event.EventListener;

import java.util.Optional;

@SpringBootApplication
@ComponentScan(
	basePackageClasses = {CourtageSpringbootApplication.class, DomainService.class},
	includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = {DomainService.class})}
)
public class CourtageSpringbootApplication {
	private static ConfigurableApplicationContext applicationContext = null;
	private int port = -1;

	public static void main(String... args) {
		applicationContext = SpringApplication.run(CourtageSpringbootApplication.class, args);
	}

	public static void stop() {
		if (applicationContext != null) {
			applicationContext.close();
			applicationContext = null;
		}
	}

	public static Optional<Integer> getPort() {
		return Optional.ofNullable(applicationContext)
			.map(ctx -> ctx.getBean(CourtageSpringbootApplication.class))
			.map(app -> app.port);
	}

	@EventListener
	public void onApplicationEvent(final ServletWebServerInitializedEvent event) {
		port = event.getWebServer().getPort();
	}
}
