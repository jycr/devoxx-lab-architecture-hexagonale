package devoxx.lab.archihexa.courtage.integrationtests.port.secondaire;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import devoxx.lab.archihexa.courtage.integrationtests.CoursBourse;
import devoxx.lab.archihexa.courtage.integrationtests.CourtageStepDefinitions;

public class BourseMock {
	public static final BourseMock INSTANCE = new BourseMock();

	private BourseMock() {
	}

	private final WireMockServer wireMockServer = new WireMockServer();

	public void start() {
		wireMockServer.start();
	}

	public void stop() {
		wireMockServer.stop();
	}

	public String getApiBourseUrl() {
		return "http://localhost:" + wireMockServer.port();
	}

	public void setCours(CoursBourse coursBourse) {
		WireMock
			.stubFor(WireMock.get(WireMock.urlEqualTo("/finance/quote/" + coursBourse.action()))
				.willReturn(WireMock.aResponse()
					.withStatus(200)
					.withHeader("Content-Type", "application/json")
					.withBody("{" +
						"\"symbol\": \"" + coursBourse.action() + "\"" +
						",\"regularMarketPrice\": " + coursBourse.valeur() +
						"}")));
	}
}
