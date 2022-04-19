package devoxx.lab.archihexa.courtage.application.quarkus.service;

import devoxx.lab.archihexa.courtage.application.quarkus.adapters.persistence.PortefeuilleRepositoryJpaImpl;
import devoxx.lab.archihexa.courtage.application.quarkus.adapters.rest.ServiceBourseHttpAdapter;
import devoxx.lab.archihexa.courtage.domain.port.primaire.Courtage;
import devoxx.lab.archihexa.courtage.domain.port.primaire.ServiceCourtage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Il n'y a pour le moment pas d'équivalent à
 * <code>org.springframework.context.annotation.ComponentScan</code>.
 * L'injection se fait donc "à la main".
 */
@ApplicationScoped
public class ServiceCourtageFactory {

	@Inject
	PortefeuilleRepositoryJpaImpl portefeuilleRepository;
	@Inject
	ServiceBourseHttpAdapter serviceBourse;
	private Courtage impl = null;

	public ServiceCourtageFactory() {
	}

	public ServiceCourtage get() {
		if (impl == null) {
			impl = new Courtage(portefeuilleRepository, serviceBourse);
		}
		return impl;
	}
}
