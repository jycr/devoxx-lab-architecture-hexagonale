package devoxx.lab.archihexa.courtage.application.quarkus;

import devoxx.lab.archihexa.courtage.domain.exception.PortefeuilleDejaExistantException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class PortefeuilleDejaExistantExceptionHandler implements ExceptionMapper<PortefeuilleDejaExistantException> {
	@Override
	public Response toResponse(PortefeuilleDejaExistantException e) {
		return Response.status(Response.Status.BAD_REQUEST).
			entity("Portefeuille déjà géré").build();
	}
}
