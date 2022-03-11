package devoxx.lab.hexagonalarchitecture.courtage.application.springboot;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.DataTableEntryDefinitionBody;
import io.cucumber.java8.Fr;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.util.Optional.ofNullable;

public class CourtageStepDefinitions implements Fr {
	private static final ObjectMapper BIGDECIMAL_MAPPER = new ObjectMapper() {
		@Override
		public Object deserialize(ObjectMapperDeserializationContext context) {
			return new BigDecimal(context.getDataToDeserialize().asString());
		}

		@Override
		public Object serialize(ObjectMapperSerializationContext context) {
			return ofNullable(context.getObjectToSerialize())
				.map(BigDecimal.class::cast)
				.map(BigDecimal::toString)
				.orElse(null);
		}
	};

	public CourtageStepDefinitions() {
		// étape 5
		Quand("on demande au service de courtage la création du portefeuille {string}", (String nomPortefeuille) -> {
			throw new io.cucumber.java8.PendingException();
		});
		Alors("l'id du portefeuille créé doit être {string}", (String nomPortefeuille) -> {
			throw new io.cucumber.java8.PendingException();
		});
		Alors("le portefeuille {string} est géré par le service de courtage", (String nomPortefeuille) -> {
			throw new io.cucumber.java8.PendingException();
		});
		Alors("le portefeuille {string} n'est pas géré par le service de courtage", (String nomPortefeuille) -> {
			throw new io.cucumber.java8.PendingException();
		});
		Alors("une exception est levée : Portefeuille déjà géré", () -> {
			throw new io.cucumber.java8.PendingException();
		});

		// étape 6
		Quand("on demande le calcul de la valeur du portefeuille {string}", (String nomPortefeuille) -> {
			throw new io.cucumber.java8.PendingException();
		});
		Alors("la valeur du portefeuille est {bigdecimal}", (BigDecimal valeur) -> {
			throw new io.cucumber.java8.PendingException();
		});
		Alors("une exception est levée : Portefeuille non géré", () -> {
			throw new io.cucumber.java8.PendingException();
		});

		// étape 7
		DataTableType(CoursBourse.CONVERTER);
		Quand("(si )les cours de bourse suivants/sont/deviennent :", (DataTable dataTable) ->
			dataTable.asList(CoursBourse.class).forEach(coursBourse -> {
				stubFor(get(urlEqualTo("/finance/quote/" + coursBourse.action))
					.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody("{" +
							"\"symbol\": \"" + coursBourse.action + "\"" +
							",\"regularMarketPrice\": " + coursBourse.valeur +
							"}")));
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

		private CoursBourse(String action, BigDecimal valeur) throws ParseException {
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

		private AjoutAction(String portefeuille, String action, int nombre) throws ParseException {
			this.portefeuille = portefeuille;
			this.action = action;
			this.nombre = nombre;
		}
	}
}
