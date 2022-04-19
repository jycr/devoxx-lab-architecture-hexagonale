package devoxx.lab.archihexa.courtage.application.quarkus;

import devoxx.lab.archihexa.courtage.domain.exception.PortefeuilleNonGereException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class PortefeuilleNonGereExceptionHandler implements ExceptionMapper<PortefeuilleNonGereException> {
	@Override
	public Response toResponse(PortefeuilleNonGereException e) {
		return Response.status(Response.Status.NOT_FOUND).
			entity("Portefeuille non géré").build();
	}
}