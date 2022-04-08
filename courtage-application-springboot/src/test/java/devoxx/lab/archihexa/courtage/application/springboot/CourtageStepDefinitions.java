package devoxx.lab.archihexa.courtage.application.springboot;

import devoxx.lab.archihexa.courtage.domain.model.Achat;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.DataTableEntryDefinitionBody;
import io.cucumber.java8.Fr;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;
import io.restassured.response.ValidatableResponse;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static devoxx.lab.archihexa.courtage.application.springboot.CucumberLifecycleHandler.getApiBourseUrl;
import static io.restassured.RestAssured.*;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

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

	private ValidatableResponse response;

	public CourtageStepDefinitions() {
		CourtageSpringbootApplication.raz();

		// étape 5
		Quand("on demande au service de courtage la création du portefeuille {string}", (String nomPortefeuille) ->
			response = when()
				.post("/courtage/portefeuilles/" + nomPortefeuille)
				.then());
		Alors("l'id du portefeuille créé doit être {string}", (String nomPortefeuille) ->
			response
				.assertThat()
				.statusCode(201)
				.header("location", "http://localhost:" + port + "/courtage/portefeuilles/" + nomPortefeuille));
		Alors("le portefeuille {string} est géré par le service de courtage", (String nomPortefeuille) -> {
			when()
				.get("/courtage/portefeuilles/" + nomPortefeuille)
				.then()
				.assertThat()
				.statusCode(200);
		});
		Alors("le portefeuille {string} n'est pas géré par le service de courtage", (String nomPortefeuille) ->
			when()
				.get("/courtage/portefeuilles/" + nomPortefeuille)
				.then()
				.assertThat()
				.statusCode(404));
		Alors("une exception est levée : Portefeuille déjà géré", () ->
			response
				.assertThat()
				.statusCode(400)
				.body(equalTo("Portefeuille déjà géré")));

		// étape 6
		Quand("on demande le calcul de la valeur du portefeuille {string}", (String nomPortefeuille) ->
			response = when()
				.get("/courtage/portefeuilles/" + nomPortefeuille + "/valorisation")
				.then());
		Alors("la valeur du portefeuille est {bigdecimal}", (BigDecimal valeur) ->
			assertThat(
				response
					.assertThat()
					.statusCode(200)
					.extract()
					.body()
					.as(BigDecimal.class, BIGDECIMAL_MAPPER)
			)
				.isEqualByComparingTo(valeur));
		Alors("une exception est levée : Portefeuille non géré", () ->
			response
				.assertThat()
				.statusCode(404)
				.body(equalTo("Portefeuille non géré")));

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
			response = given().spec(
					new RequestSpecBuilder()
						.setBaseUri(getApiBourseUrl())
						.setBasePath("/finance/quote/")
						.build()
				).when()
				.get(nomAction)
				.then();
		});
		Alors("la valeur récupérée pour l'action est {bigdecimal}", (BigDecimal valeurAction) ->
			assertThat(
				new BigDecimal(
					response
						.assertThat()
						.statusCode(200)
						.extract()
						.jsonPath()
						.getString("regularMarketPrice")
				)
			)
				.isEqualByComparingTo(valeurAction));

		DataTableType(AjoutAction.CONVERTER);
		Quand("^on demande au service de courtage d'ajouter (?:l'|les )actions? suivantes? :$", (DataTable dataTable) ->
			dataTable.asList(AjoutAction.class).forEach(ajoutAction -> {
				response = given()
					.contentType(ContentType.JSON)
					.body(new Achat(ajoutAction.action, ajoutAction.nombre))
					.when()
					.post("/courtage/portefeuilles/" + ajoutAction.portefeuille + "/actions")
					.then();
			})
		);
		Quand("on demande au service de courtage le calcul de la valeur de tous les portefeuilles", () -> {
			response = when()
				.get("/courtage/portefeuilles/avoirs")
				.then();
		});
		Alors("la valeur pour l'ensemble des portefeuilles est {bigdecimal}", (BigDecimal valeurPortefeuilles) -> assertThat(
			response
				.assertThat()
				.statusCode(200)
				.extract()
				.body()
				.as(BigDecimal.class, BIGDECIMAL_MAPPER)
		)
			.isEqualByComparingTo(valeurPortefeuilles));
		// étape 8
		Alors("une exception est levée : Donnée erronée avec le message {string}", (String message) ->
			response
				.assertThat()
				.statusCode(400)
				.body(equalTo("Donnée(s) erronée(s): \n" + "\t" + message)));
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
