package devoxx.lab.hexagonalarchitecture.courtage.domain.port.primaire;

import devoxx.lab.hexagonalarchitecture.courtage.domain.exception.PortefeuilleDejaExistantException;
import devoxx.lab.hexagonalarchitecture.courtage.domain.exception.PortefeuilleNonGereException;
import devoxx.lab.hexagonalarchitecture.courtage.domain.metier.Portefeuille;
import devoxx.lab.hexagonalarchitecture.courtage.domain.port.secondaire.PortefeuilleRepository;
import devoxx.lab.hexagonalarchitecture.courtage.domain.service.CourtageService;
import devoxx.lab.hexagonalarchitecture.courtage.domain.service.impl.Courtage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.DataTableEntryDefinitionBody;
import io.cucumber.java8.Fr;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CourtageStepDefinitions implements Fr {
	private BigDecimal valeur;
	private Exception thrownException;
	private Portefeuille portefeuille;
	private CourtageService serviceCourtage;
	private final PortefeuilleRepository repo = new PortefeuilleRepositoryMock();

	public CourtageStepDefinitions() {
		// étape 1
		Soit("un service de courtage",
			() -> this.serviceCourtage = new Courtage(repo));
		Quand("on demande au service de courtage la création du portefeuille {string}",
			(String idPortefeuille) -> {
				try {
					portefeuille = this.serviceCourtage.creerPortefeuille(idPortefeuille);
				} catch (Exception e) {
					thrownException = e;
				}
			});
		Alors("l'id du portefeuille créé doit être {string}",
			(String idPortefeuille) -> assertThat(portefeuille.getId()).isEqualTo(idPortefeuille));
		Alors("le portefeuille {string} est géré par le service de courtage",
			(String idPortefeuille) -> assertThat(serviceCourtage.existe(idPortefeuille)).isTrue());

		// étape 2
		Alors("le portefeuille {string} n'est pas géré par le service de courtage",
			(String idPortefeuille) -> assertThat(serviceCourtage.existe(idPortefeuille)).isFalse());
		Alors("une exception est levée : Portefeuille déjà géré",
			() -> assertThat(thrownException).isInstanceOf(PortefeuilleDejaExistantException.class));
		Quand("on demande le calcul de la valeur du portefeuille {string}", (String idPortefeuille) -> {
			try {
				valeur = serviceCourtage.valeurPortefeuille(idPortefeuille);
			} catch (Exception e) {
				thrownException = e;
			}
		});
		Alors("la valeur du portefeuille est {bigdecimal}",
			(BigDecimal valeurAttendue) -> assertThat(valeur).isEqualByComparingTo(valeurAttendue));
		Alors("une exception est levée : Portefeuille non géré",
			() -> assertThat(thrownException).isInstanceOf(PortefeuilleNonGereException.class));

		// étape 3
		DataTableType(CoursBourse.CONVERTER);
		Soit("les cours de bourse suivants/sont :", (DataTable dataTable) ->
			dataTable.asList(CoursBourse.class).forEach(coursBourse -> {
				throw new io.cucumber.java8.PendingException();
			})
		);
		Quand("on demande au service de bourse la valeur de l'action {string}", (String nomAction) -> {
			throw new io.cucumber.java8.PendingException();
		});
		Alors("la valeur récupérée pour l'action est {bigdecimal}", (BigDecimal valeurAction) -> {
			throw new io.cucumber.java8.PendingException();
		});
		DataTableType(AjoutAction.CONVERTER);
		Quand("^on demande au service de courtage d'ajouter (?:l'|les )actions? suivantes? :$", (DataTable dataTable) ->
			dataTable.asList(AjoutAction.class).forEach(ajoutAction -> {
				throw new io.cucumber.java8.PendingException();
			})
		);

		// étape 4
		Quand("on demande au service de courtage le calcul de la valeur de tous les portefeuilles", () -> {
			throw new io.cucumber.java8.PendingException();
		});
		Alors("la valeur pour l'ensemble des portefeuilles est {bigdecimal}", (BigDecimal valeurPortefeuilles) -> {
			throw new io.cucumber.java8.PendingException();
		});
	}

	private static class CoursBourse {
		private static final NumberFormat NF = NumberFormat.getInstance(Locale.FRENCH);
		static final DataTableEntryDefinitionBody<CoursBourse> CONVERTER = (Map<String, String> row) -> new CoursBourse(
			row.get("Action"),
			new BigDecimal(NF.parse(row.get("Valeur")).toString())
		);
		final String action;
		final BigDecimal valeur;

		private CoursBourse(String action, BigDecimal valeur) {
			this.action = action;
			this.valeur = valeur;
		}
	}

	private static class AjoutAction {
		static final DataTableEntryDefinitionBody<AjoutAction> CONVERTER = (Map<String, String> row) -> new AjoutAction(
			row.get("Portefeuille"),
			row.get("Action"),
			Integer.parseInt(row.get("Nombre"))
		);
		final String portefeuille;
		final String action;
		final int nombre;

		private AjoutAction(String portefeuille, String action, int nombre) {
			this.portefeuille = portefeuille;
			this.action = action;
			this.nombre = nombre;
		}
	}
}
