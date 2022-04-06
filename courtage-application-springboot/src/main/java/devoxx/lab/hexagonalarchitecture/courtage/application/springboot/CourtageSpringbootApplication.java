package devoxx.lab.hexagonalarchitecture.courtage.application.springboot;

import devoxx.lab.hexagonalarchitecture.courtage.application.springboot.adapters.mixin.AchatMixIn;
import devoxx.lab.hexagonalarchitecture.courtage.domain.DomainService;
import devoxx.lab.hexagonalarchitecture.courtage.domain.model.Achat;
import devoxx.lab.hexagonalarchitecture.courtage.domain.port.secondaire.PortefeuilleRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.event.EventListener;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

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

	static void raz() {
		Optional.ofNullable(applicationContext)
			.map(ctx -> ctx.getBean(PortefeuilleRepository.class))
			.ifPresent(PortefeuilleRepository::purge);
	}

	@EventListener
	public void onApplicationEvent(final ServletWebServerInitializedEvent event) {
		port = event.getWebServer().getPort();
	}

	@Bean
	Jackson2ObjectMapperBuilder objectMapperBuilder() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		// Configure the builder to suit your needs
		builder.mixIn(Achat.class, AchatMixIn.class);
		return builder;
	}
}
