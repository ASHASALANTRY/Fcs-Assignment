package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.adapters.mapper.WarehouseMapper;
import com.fulfilment.application.monolith.warehouses.domain.ports.ArchiveWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import com.warehouse.api.WarehouseResource;
import com.warehouse.api.beans.Warehouse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.WebApplicationException;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Objects;

@RequestScoped
public class WarehouseResourceImpl implements WarehouseResource {
    private static final Logger LOG = Logger.getLogger(WarehouseResourceImpl.class);

    @Inject
    CreateWarehouseOperation createWarehouse;

    @Inject
    WarehouseRepository warehouseRepository;

    @Inject
    ArchiveWarehouseOperation archiveWarehouseOperation;
    @Inject
    WarehouseStore warehouseStore;
    @Inject
    ReplaceWarehouseOperation replaceWarehouseOperation;

    @Inject
    WarehouseMapper warehouseMapper;


    /**
     * Lists all active (non-archived) warehouse units.
     */
    @Override
    public List<Warehouse> listAllWarehousesUnits() {
        LOG.debug("Received request to list all warehouse units");
        return warehouseStore.getAll()
                .stream()
                .map(warehouseMapper::domainToApi)
                .toList();
    }


    /**
     * Creates a new warehouse unit.
     *
     * Delegates validation and creation logic to the domain layer.
     */
    @Override
    public Warehouse createANewWarehouseUnit(@NotNull Warehouse data) {
        LOG.infof("Received request to create warehouse with businessUnitCode=%s",
                data.getId());
        createWarehouse.create(warehouseMapper.toDomain(data));
        LOG.infof("Warehouse created successfully with businessUnitCode=%s",
                data.getId());
        return data;
    }

    /**
     * Retrieves a warehouse unit by database ID.
     *
     * Returns 404 if warehouse does not exist or is archived.
     */
    @Override
    public Warehouse getAWarehouseUnitByID(String id) {
        LOG.debugf("Fetching warehouse by id=%s", id);
        Long dbId = parseId(id);
        DbWarehouse db = warehouseRepository.findById(dbId);
        if (db == null || db.archivedAt != null) {
            LOG.warnf("Warehouse not found or archived for id=%s", id);
            throw new WebApplicationException(404);
        }
        return warehouseMapper.toApi(db);
    }
    /**
     * Archives a warehouse unit.
     *
     * Marked transactional to ensure consistency during state change.
     */
    @Transactional
    @Override
    public void archiveAWarehouseUnitByID(String id) {
        LOG.infof("Archiving warehouse with id=%s", id);
        Long dbId = parseId(id);
        DbWarehouse db = warehouseRepository.findById(dbId);
        if (db == null || db.archivedAt != null) {
            LOG.warnf("Warehouse not found or already archived for id=%s", id);
            throw new WebApplicationException(404);
        }
        archiveWarehouseOperation.archive(warehouseMapper.toDomain(db));
        LOG.infof("Warehouse archived successfully with id=%s", id);
    }


  @Override
  public Warehouse replaceTheCurrentActiveWarehouse(
      String businessUnitCode, @NotNull Warehouse data) {
if(Objects.nonNull(data.getBusinessUnitCode()))
    throw new BadRequestException("BusinessUnitCode should be null as its a replace operation");
data.setBusinessUnitCode(businessUnitCode);
      replaceWarehouseOperation.replace(warehouseMapper.toDomain(data));
      return data;
  }

  private Warehouse toWarehouseResponse(
      com.fulfilment.application.monolith.warehouses.domain.models.Warehouse warehouse) {
    var response = new Warehouse();
    response.setBusinessUnitCode(warehouse.businessUnitCode);
    response.setLocation(warehouse.location);
    response.setCapacity(warehouse.capacity);
    response.setStock(warehouse.stock);

    return response;
  }

    /**
     * Parses and validates warehouse ID from request path.
     *
     * Throws 400 Bad Request for invalid numeric values.
     */
    private Long parseId(String id) {
        try {
            return Long.valueOf(id);
        } catch (NumberFormatException e) {
            LOG.warnf("Invalid warehouse id received: %s", id);
            throw new WebApplicationException("Invalid warehouse id: " + id, 400);
        }
    }

}
