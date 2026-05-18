package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.location.LocationGateway;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.apache.commons.lang.StringUtils;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Domain use case responsible for creating a new Warehouse.
 *
 * This class enforces all business invariants related to warehouse creation
 * and coordinates with required domain ports.
 */
@ApplicationScoped
public class CreateWarehouseUseCase implements CreateWarehouseOperation {

    private static final Logger LOG = Logger.getLogger(CreateWarehouseUseCase.class);

    private final WarehouseStore warehouseStore;
    private final LocationGateway locationGateway;
    public CreateWarehouseUseCase(WarehouseStore warehouseStore, LocationGateway locationGateway) {
        this.warehouseStore = warehouseStore;
        this.locationGateway = locationGateway;
    }

    /**
     * Creates a new warehouse after validating all business rules.
     *
     * Business rules enforced:
     * 1. Business unit code must be unique
     * 2. Location must exist and be valid
     * 3. Location warehouse count must not exceed limit
     * 4. Warehouse capacity must respect location capacity
     * 5. Stock must not exceed warehouse capacity
     *
     * Transactional to ensure atomic validation and persistence.
     */
    @Transactional
    @Override
    public void create(Warehouse warehouse) {

        LOG.debugf(
                "Attempting to create warehouse with businessUnitCode=%s",
                warehouse.businessUnitCode
        );
        if(Objects.isNull(warehouse.businessUnitCode) || Objects.isNull(warehouse.location) || Objects.isNull(warehouse.capacity)||Objects.isNull(warehouse.stock))
        {
            LOG.warnf(
                    "Warehouse creation failed. businessUnitCode,location,capacity,stock cannot be null"
            );
            throw new IllegalArgumentException(
                    "Warehouse creation failed.  businessUnitCode,location,capacity,stock cannot be null");
        }
        if( warehouse.capacity<=0 ||warehouse.stock<0)
        {
            LOG.warnf(
                    "Warehouse creation failed. capacity cannot be 0 or less than 0. Stock cannot be less than 0"
            );
            throw new IllegalArgumentException(
                    "Warehouse creation failed. capacity cannot be 0 or less than 0. Stock cannot be less than 0");
        }

        // 1. Business unit uniqueness check
        Warehouse existingWarehouse=warehouseStore.findByBusinessUnitCode(
                warehouse.businessUnitCode);
        if ( Objects.nonNull(existingWarehouse) && Objects.isNull(existingWarehouse.archivedAt)) {
            LOG.warnf(
                    "Warehouse creation failed. Business unit code already exists: %s",
                    warehouse.businessUnitCode
            );
            throw new IllegalArgumentException(



                    "Business unit code already exists");
        }

        // 2. Resolve and validate location via domain gateway
        Location location =
                locationGateway.resolveByIdentifier(warehouse.location);

        LOG.debugf(
                "Resolved location %s with maxWarehouses=%d, maxCapacity=%d",
                warehouse.location,
                location.maxNumberOfWarehouses,
                location.maxCapacity
        );
        // 3. Enforce maximum number of warehouses per location
        long activeWarehouseCount  =
                warehouseStore.countActiveByLocation(warehouse.location);
        LOG.warnf(
                "Warehouse creation failed. Max warehouses reached for location=%s",
                warehouse.location
        );
        if (activeWarehouseCount  >= location.maxNumberOfWarehouses) {
            throw new IllegalArgumentException(
                    "Max warehouses reached for location");
        }

        // 4. Validate warehouse capacity against location capacity
        if (warehouse.capacity > location.maxCapacity) {
            LOG.warnf(
                    "Warehouse creation failed. Capacity %d exceeds location maxCapacity %d for location=%s",
                    warehouse.capacity,
                    location.maxCapacity,
                    warehouse.location
            );
            throw new IllegalArgumentException(
                    "Warehouse capacity exceeds location capacity");
        }

        // 5. Validate stock does not exceed warehouse capacity
        if (warehouse.stock > warehouse.capacity) {
            LOG.warnf(
                    "Warehouse creation failed. Stock %d exceeds capacity %d for businessUnitCode=%s",
                    warehouse.stock,
                    warehouse.capacity,
                    warehouse.businessUnitCode
            );
            throw new IllegalArgumentException(
                    "Stock exceeds warehouse capacity");
        }
        // Apply domain state (creation timestamp controlled by domain)
        warehouse.createdAt = LocalDateTime.now();

        // Persist warehouse via domain port
        warehouseStore.create(warehouse);

        LOG.infof(
                "Warehouse created successfully with businessUnitCode=%s at location=%s",
                warehouse.businessUnitCode,
                warehouse.location  );  }
}
