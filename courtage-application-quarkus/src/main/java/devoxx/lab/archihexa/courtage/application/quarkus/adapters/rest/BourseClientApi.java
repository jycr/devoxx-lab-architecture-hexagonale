package devoxx.lab.archihexa.courtage.application.quarkus.adapters.rest;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@RegisterRestClient(configKey = "bourse-api")
public interface BourseClientApi {
	@GET
	@Path("/finance/quote/{action}")
	CoursBourse recupererCours(@PathParam("action") String action);
}
