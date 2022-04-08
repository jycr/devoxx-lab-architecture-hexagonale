package devoxx.lab.archihexa.courtage.application.springboot;

import devoxx.lab.archihexa.courtage.application.springboot.adapters.mixin.AchatJsonDto;
import devoxx.lab.archihexa.courtage.domain.model.Achat;
import devoxx.lab.archihexa.courtage.domain.port.primaire.ServiceCourtage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.Optional;

@SpringBootApplication
@ComponentScan(
	basePackageClasses = {CourtageSpringbootApplication.class, ServiceCourtage.class}
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

	static void raz() {
	}

	@EventListener
	public void onApplicationEvent(final ServletWebServerInitializedEvent event) {
		port = event.getWebServer().getPort();
	}

	@Bean
	Jackson2ObjectMapperBuilder objectMapperBuilder() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		// Configure the builder to suit your needs
		builder.mixIn(Achat.class, AchatJsonDto.class);
		return builder;
	}
}
