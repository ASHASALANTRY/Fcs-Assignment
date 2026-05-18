package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.adapters.restapi.WarehouseResourceImpl;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.ArchiveWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Objects;
/**
 * Domain use case responsible for archiving a warehouse.
 *
 * Encapsulates business rules related to warehouse archival
 * and remains independent of infrastructure concerns.
 */
@ApplicationScoped
public class ArchiveWarehouseUseCase implements ArchiveWarehouseOperation {
    private static final Logger LOG = Logger.getLogger(ArchiveWarehouseUseCase.class);
    private final WarehouseStore warehouseStore;

    public ArchiveWarehouseUseCase(WarehouseStore warehouseStore) {
        this.warehouseStore = warehouseStore;
    }

    /**
     * Archives an existing warehouse.
     *
     * Business rules:
     * - Warehouse must exist
     * - Archival timestamp is set by the domain
     *
     * Transactional to ensure atomic read-update behavior.
     */
    @Transactional
    @Override
    public void archive(Warehouse warehouse) {

        LOG.debugf(
                "Attempting to archive warehouse with businessUnitCode=%s",
                warehouse.businessUnitCode
        );

        // Load the current state from persistence via domain port

        Warehouse existing =
                warehouseStore.findByBusinessUnitCode(
                        warehouse.businessUnitCode
                );

        // Business invariant: warehouse must exist before archival
        if (existing == null || existing.archivedAt != null) {
            LOG.warnf(
                    "Archive failed. Warehouse not found  or already archived for businessUnitCode=%s",
                    warehouse.businessUnitCode
            );
            throw new IllegalArgumentException(
                    "Warehouse not found: " + warehouse.businessUnitCode
            );
        }
        // Apply domain state change
        existing.archivedAt = LocalDateTime.now();

        // Persist updated state
        warehouseStore.update(existing);

        LOG.infof(
                "Warehouse archived successfully with businessUnitCode=%s",
                warehouse.businessUnitCode
        );
    }
}
