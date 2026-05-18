package com.fulfilment.application.monolith.stores;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Status;
import jakarta.transaction.Synchronization;
import jakarta.transaction.TransactionSynchronizationRegistry;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.List;
import org.jboss.logging.Logger;

@Path("stores")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class StoreResource {

    @Inject LegacyStoreManagerGateway legacyStoreManagerGateway;
    @Inject
    TransactionSynchronizationRegistry txRegistry;

    private static final Logger LOGGER = Logger.getLogger(StoreResource.class.getName());

    @GET
    public List<Store> get() {
        return Store.listAll(Sort.by("name"));
    }

    @GET
    @Path("{id}")
    public Store getSingle(Long id) {
        Store entity = Store.findById(id);
        if (entity == null) {
            LOGGER.warnf("Store not found with id=%d", id);
            throw new WebApplicationException("Store with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    @POST
    @Transactional
    public Response create(Store store) {
        if (store.id != null) {
            LOGGER.warn("Create store failed: ID must not be provided");

            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        store.persist();
        LOGGER.debugf("Store persisted with generated id=%d", store.id);

        registerAfterCommit(() ->{
            LOGGER.infof("Propagating store creation to legacy system (id=%d)", store.id);
            legacyStoreManagerGateway.createStoreOnLegacySystem(store);}
        );

        return Response.ok(store).status(201).build();
    }


    @PUT
    @Path("{id}")
    @Transactional
    public Store update(@PathParam("id") Long id, Store updatedStore) {
        if (updatedStore.name == null) {
            LOGGER.warn("Update store failed: store name is missing");
            throw new WebApplicationException("Store Name was not set on request.", 422);
        }

        Store entity = Store.findById(id);

        if (entity == null) {
            LOGGER.warnf("Update failed: store not found with id=%d", id);
            throw new WebApplicationException("Store with id of " + id + " does not exist.", 404);
        }

        entity.name = updatedStore.name;
        entity.quantityProductsInStock = updatedStore.quantityProductsInStock;

        registerAfterCommit(() ->{
                    LOGGER.infof("Propagating store update to legacy system (id=%d)", id);

                    legacyStoreManagerGateway.updateStoreOnLegacySystem(entity);
                }
        );

        return entity;
    }


    @PATCH
    @Path("{id}")
    @Transactional
    public Store patch(@PathParam("id") Long id, Store updatedStore) {

        Store entity = Store.findById(id);

        if (entity == null) {
            LOGGER.warnf("Patch failed: store not found with id=%d", id);
            throw new WebApplicationException("Store with id of " + id + " does not exist.", 404);
        }

        if (updatedStore.name != null) {
            entity.name = updatedStore.name;
        }

        if (updatedStore.quantityProductsInStock != 0) {
            entity.quantityProductsInStock = updatedStore.quantityProductsInStock;
        }

        registerAfterCommit(() ->
                {
                    LOGGER.infof("Propagating store patch to legacy system (id=%d)", id);

                    legacyStoreManagerGateway.updateStoreOnLegacySystem(entity);
                }
        );

        return entity;
    }


    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        Store entity = Store.findById(id);
        if (entity == null) {
            LOGGER.warnf("Delete failed: store not found with id=%d", id);
            throw new WebApplicationException("Store with id of " + id + " does not exist.", 404);
        }
        entity.delete();
        return Response.status(204).build();
    }

    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

        @Inject ObjectMapper objectMapper;

        @Override
        public Response toResponse(Exception exception) {
            LOGGER.error("Failed to handle request", exception);

            int code = 500;
            if (exception instanceof WebApplicationException) {

                code = ((WebApplicationException) exception).getResponse().getStatus();
            }

            ObjectNode exceptionJson = objectMapper.createObjectNode();
            exceptionJson.put("exceptionType", exception.getClass().getName());
            exceptionJson.put("code", code);

            if (exception.getMessage() != null) {
                exceptionJson.put("error", exception.getMessage());
            }

            return Response.status(code).entity(exceptionJson).build();
        }
    }

    /**
     * Ensures that side-effects to the legacy system are executed
     * only after the current transaction has been successfully committed.
     * This prevents propagating uncommitted or rolled-back state.
     */
    private void registerAfterCommit(Runnable action) {
        txRegistry.registerInterposedSynchronization(new Synchronization() {
            @Override
            public void beforeCompletion() {}

            @Override
            public void afterCompletion(int status) {
                if (status == Status.STATUS_COMMITTED) {
                    action.run();
                }else
                    LOGGER.debug("Transaction rolled back; skipping legacy synchronization");

            }
        });
    }

}
