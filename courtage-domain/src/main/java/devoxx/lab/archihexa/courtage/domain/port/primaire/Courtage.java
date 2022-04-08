package devoxx.lab.archihexa.courtage.domain.port.primaire;

import devoxx.lab.archihexa.courtage.domain.exception.PortefeuilleDejaExistantException;
import devoxx.lab.archihexa.courtage.domain.exception.PortefeuilleNonGereException;
import devoxx.lab.archihexa.courtage.domain.model.Achat;
import devoxx.lab.archihexa.courtage.domain.model.Portefeuille;
import devoxx.lab.archihexa.courtage.domain.port.secondaire.PortefeuilleRepository;
import devoxx.lab.archihexa.courtage.domain.port.secondaire.ServiceBourse;

import javax.annotation.ManagedBean;
import java.math.BigDecimal;

@ManagedBean
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

	private BigDecimal calculerValeurPortefeuille(Portefeuille portefeuille) {
		return portefeuille.getActions().entrySet().stream()
			.map(entry -> serviceBourse
				// entry.getKey(): nom de l'action
				.recupererCours(entry.getKey())
				// entry.getValue() : nombre d'action
				.multiply(new BigDecimal(entry.getValue())))
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	@Override
	public BigDecimal calculerValeurPortefeuille(String nomPortefeuille) throws PortefeuilleNonGereException {
		return calculerValeurPortefeuille(
			portefeuilleRepository.recupere(nomPortefeuille)
				.orElseThrow(PortefeuilleNonGereException::new)
		);
	}

	@Override
	public void ajouteAction(String nomPortefeuille, Achat achat) throws PortefeuilleNonGereException {
		Portefeuille portefeuille = portefeuilleRepository.recupere(nomPortefeuille)
			.orElseThrow(PortefeuilleNonGereException::new);
		portefeuille.ajouterAction(achat);
		portefeuilleRepository.sauvegarde(portefeuille);
	}

	@Override
	public BigDecimal calculerValeurEnsemblePortefeuilles() {
		return portefeuilleRepository.recupereTous()
			.map(this::calculerValeurPortefeuille)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
