package devoxx.lab.archihexa.courtage.application.quarkus;

import devoxx.lab.archihexa.courtage.application.quarkus.service.ServiceCourtageFactory;
import devoxx.lab.archihexa.courtage.domain.exception.PortefeuilleDejaExistantException;
import devoxx.lab.archihexa.courtage.domain.exception.PortefeuilleNonGereException;
import devoxx.lab.archihexa.courtage.domain.model.Achat;
import devoxx.lab.archihexa.courtage.domain.port.primaire.ServiceCourtage;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

import static java.util.Optional.ofNullable;

@Path("/courtage")
@Produces(MediaType.TEXT_PLAIN)
public class CourtageResource {
	private final ServiceCourtage serviceCourtage;

	@Inject
	public CourtageResource(ServiceCourtageFactory serviceCourtageFactory) {
		this.serviceCourtage = serviceCourtageFactory.get();
	}

	@POST
	@Transactional
	@Path("/portefeuilles/{nomPortefeuille}")
	public Response creationPortefeuille(
		@PathParam(value = "nomPortefeuille") String nomPortefeuille,
		@Context UriInfo uriInfo
	) throws PortefeuilleDejaExistantException {
		serviceCourtage.creerPortefeuille(nomPortefeuille);
		URI uri = uriInfo.getAbsolutePathBuilder().build();
		return Response.created(uri).build();
	}

	@POST
	@Transactional
	@Path("/portefeuilles/{nomPortefeuille}/actions")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response ajoutActionsDansPortefeuille(
		@PathParam(value = "nomPortefeuille") String nomPortefeuille,
		@Valid Achat achat
	) throws PortefeuilleNonGereException {
		serviceCourtage.ajouteAction(nomPortefeuille, achat);
		return Response.ok().build();
	}

	@GET
	@Path("/portefeuilles/{nomPortefeuille}")
	public Response portefeuilleExiste(@PathParam(value = "nomPortefeuille") String nomPortefeuille) throws PortefeuilleNonGereException {
		if (serviceCourtage.gere(nomPortefeuille)) {
			return Response.ok().build();
		}
		throw new PortefeuilleNonGereException();
	}

	@GET
	@Path("/portefeuilles/{nomPortefeuille}/valorisation")
	public Response calculValorisationPortefeuille(@PathParam(value = "nomPortefeuille") String nomPortefeuille) throws PortefeuilleNonGereException {
		return Response.ok(serviceCourtage.calculerValeurPortefeuille(nomPortefeuille).toString()).build();
	}

	@GET
	@Path("/portefeuilles/avoirs")
	public Response valeurEnsemblePortefeuilles() {
		return Response.ok(serviceCourtage.calculerValeurEnsemblePortefeuilles().toString()).build();
	}

	@GET
	@Path("/version")
	public String version() {
		return ofNullable(CourtageResource.class.getPackage().getImplementationVersion())
			.orElse("DEV");
	}
}
