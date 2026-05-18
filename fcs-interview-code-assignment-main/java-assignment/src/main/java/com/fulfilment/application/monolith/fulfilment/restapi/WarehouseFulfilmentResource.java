package com.fulfilment.application.monolith.fulfilment.restapi;

import com.fulfilment.application.monolith.fulfilment.model.AssignWarehouseDto;
import com.fulfilment.application.monolith.fulfilment.usecases.WarehouseFulfilmentUsecase;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/fulfilment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WarehouseFulfilmentResource {

    @Inject
    WarehouseFulfilmentUsecase warehouseFulfilmentUsecase;

    @POST
    public Response assign(AssignWarehouseDto data) {
        AssignWarehouseDto result = warehouseFulfilmentUsecase.assign(data);

        if (result == null) {
            return Response.noContent().build();
        }

        return Response.status(Response.Status.CREATED).entity(result).build();
    }
}
