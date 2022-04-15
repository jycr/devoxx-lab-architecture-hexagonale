package devoxx.lab.archihexa.courtage.integrationtests;

import devoxx.lab.archihexa.courtage.domain.model.Achat;
import devoxx.lab.archihexa.courtage.integrationtests.port.secondaire.BourseMock;
import io.cucumber.java.DataTableType;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Quand;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;

public class CourtageStepDefinitions {
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

	// étape 5
	@Quand("on demande au service de courtage la création du portefeuille {string}")
	public void on_demande_au_service_de_courtage_la_creation_du_portefeuille(String nomPortefeuille) {
		response = RestAssured.when()
			.post("/courtage/portefeuilles/" + nomPortefeuille)
			.then();
	}

	@Alors("l'id du portefeuille créé doit être {string}")
	public void l_id_du_portefeuille_cree_doit_etre(String nomPortefeuille) {
		response
			.assertThat()
			.statusCode(201)
			.header("location", "http://localhost:" + RestAssured.port + "/courtage/portefeuilles/" + nomPortefeuille);
	}

	@Alors("le portefeuille {string} est géré par le service de courtage")
	public void le_portefeuille_est_gere_par_le_service_de_courtage(String nomPortefeuille) {
		RestAssured.when()
			.get("/courtage/portefeuilles/" + nomPortefeuille)
			.then()
			.assertThat()
			.statusCode(200);
	}

	@Alors("le portefeuille {string} n'est pas géré par le service de courtage")
	public void le_portefeuille_n_est_pas_gere_par_le_service_de_courtage(String nomPortefeuille) {
		RestAssured.when()
			.get("/courtage/portefeuilles/" + nomPortefeuille)
			.then()
			.assertThat()
			.statusCode(404);
	}

	@Alors("une exception est levée : Portefeuille déjà géré")
	public void une_exception_est_levee_portefeuille_deja_gere() {
		response
			.assertThat()
			.statusCode(400)
			.body(Matchers.equalTo("Portefeuille déjà géré"));
	}

	// étape 6
	@Quand("on demande le calcul de la valeur du portefeuille {string}")
	public void on_demande_le_calcul_de_la_valeur_du_portefeuille(String nomPortefeuille) {
		response = RestAssured.when()
			.get("/courtage/portefeuilles/" + nomPortefeuille + "/valorisation")
			.then();
	}

	@Alors("la valeur du portefeuille est {bigdecimal}")
	public void la_valeur_du_portefeuille_est(BigDecimal valeur) {
		assertThat(
			response
				.assertThat()
				.statusCode(200)
				.extract()
				.body()
				.as(BigDecimal.class, BIGDECIMAL_MAPPER)
		)
			.isEqualByComparingTo(valeur);
	}

	@Alors("une exception est levée : Portefeuille non géré")
	public void une_exception_est_levee_portefeuille_non_gere() {
		response
			.assertThat()
			.statusCode(404)
			.body(Matchers.equalTo("Portefeuille non géré"));
	}

	// étape 7
	@Quand("(si )les cours de bourse suivants/sont/deviennent :")
	public void les_cours_de_bourse_suivants(List<CoursBourse> listCoursBourse) {
		listCoursBourse.forEach(BourseMock.INSTANCE::setCours);
	}

	@Quand("on demande au service de bourse la valeur de l'action {string}")
	public void on_demande_au_service_de_bourse_la_valeur_de_l_action(String nomAction) {
		response = RestAssured.given().spec(
				new RequestSpecBuilder()
					.setBaseUri(BourseMock.INSTANCE.getApiBourseUrl())
					.setBasePath("/finance/quote/")
					.build()
			).when()
			.get(nomAction)
			.then();
	}

	@Alors("la valeur récupérée pour l'action est {bigdecimal}")
	public void la_valeur_récupérée_pour_l_action_est(BigDecimal valeurAction) {
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
			.isEqualByComparingTo(valeurAction);
	}

	@Quand("^on demande au service de courtage d'ajouter (?:l'|les )actions? suivantes? :$")
	public void on_demande_au_service_de_courtage_d_ajouter_l_action_suivante(List<AjoutAction> listAjoutAction) {
		listAjoutAction.forEach(ajoutAction ->
			response = RestAssured.given()
				.contentType(ContentType.JSON)
				.body(new Achat(ajoutAction.action(), ajoutAction.nombre()))
				.when()
				.post("/courtage/portefeuilles/" + ajoutAction.portefeuille() + "/actions")
				.then());
	}

	@Quand("on demande au service de courtage le calcul de la valeur de tous les portefeuilles")
	public void on_demande_au_service_de_courtage_le_calcul_de_la_valeur_de_tous_les_portefeuilles() {
		response = RestAssured.when()
			.get("/courtage/portefeuilles/avoirs")
			.then();
	}

	@Alors("la valeur pour l'ensemble des portefeuilles est {bigdecimal}")
	public void la_valeur_pour_l_ensemble_des_portefeuilles_est(BigDecimal valeurPortefeuilles) {
		assertThat(
			response
				.assertThat()
				.statusCode(200)
				.extract()
				.body()
				.as(BigDecimal.class, BIGDECIMAL_MAPPER)
		)
			.isEqualByComparingTo(valeurPortefeuilles);
	}

	// étape 8
	@Alors("une exception est levée : Donnée erronée avec le message {string}")
	public void une_exception_est_levee_donnee_erronee_avec_le_message(String message) {
		response
			.assertThat()
			.statusCode(400)
			.body(Matchers.equalTo("Donnée(s) erronée(s): \n" + "\t" + message));
	}

	@DataTableType
	public CoursBourse convertCoursBourse(Map<String, String> row) {
		return CoursBourse.fromValues(row);
	}

	@DataTableType
	public AjoutAction convertAjoutAction(Map<String, String> row) {
		return AjoutAction.fromValues(row);
	}
}
