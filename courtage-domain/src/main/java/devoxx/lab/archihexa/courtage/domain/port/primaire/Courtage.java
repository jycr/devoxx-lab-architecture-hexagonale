package devoxx.lab.archihexa.courtage.domain.port.primaire;

import devoxx.lab.archihexa.courtage.domain.exception.PortefeuilleDejaExistantException;
import devoxx.lab.archihexa.courtage.domain.exception.PortefeuilleNonGereException;
import devoxx.lab.archihexa.courtage.domain.model.Achat;
import devoxx.lab.archihexa.courtage.domain.model.Portefeuille;
import devoxx.lab.archihexa.courtage.domain.port.secondaire.PortefeuilleRepository;
import devoxx.lab.archihexa.courtage.domain.port.secondaire.ServiceBourse;

import java.math.BigDecimal;

public class Courtage implements ServiceCourtage {
	private final PortefeuilleRepository portefeuilleRepository;
	private final ServiceBourse serviceBourse;

	public Courtage(PortefeuilleRepository portefeuilleRepository, ServiceBourse serviceBourse) {
		this.portefeuilleRepository = portefeuilleRepository;
		this.serviceBourse = serviceBourse;
	}

	@Override
	public Portefeuille creerPortefeuille(String nomPortefeuille) throws PortefeuilleDejaExistantException {
		if (gere(nomPortefeuille)) {
			throw new PortefeuilleDejaExistantException();
		}
		Portefeuille nouveauPortefeuille = new Portefeuille(nomPortefeuille);
		this.portefeuilleRepository.sauvegarde(nouveauPortefeuille);
		return nouveauPortefeuille;
	}

	@Override
	public boolean gere(String nomPortefeuille) {
		return portefeuilleRepository.existe(nomPortefeuille);
	}

	@Override
	public BigDecimal calculerValeurPortefeuille(String nomPortefeuille) throws PortefeuilleNonGereException {
		return portefeuilleRepository.recupere(nomPortefeuille)
			.orElseThrow(PortefeuilleNonGereException::new)
			.getActions().entrySet().stream()
			.map(entry -> serviceBourse
				// entry.getKey(): nom de l'action
				.recupererCours(entry.getKey())
				// entry.getValue() : nombre d'action
				.multiply(new BigDecimal(entry.getValue())))
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	@Override
	public void ajouteAction(String nomPortefeuille, Achat achat) throws PortefeuilleNonGereException {
		portefeuilleRepository.recupere(nomPortefeuille)
			.orElseThrow(PortefeuilleNonGereException::new)
			.ajouterAction(achat)
		;
	}
}
