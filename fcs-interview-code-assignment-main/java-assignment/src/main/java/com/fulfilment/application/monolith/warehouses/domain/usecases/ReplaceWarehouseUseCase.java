package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.ArchiveWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

/**
 * Domain use case responsible for replacing an existing warehouse definition.
 *
 * Replacement semantics:
 * - Warehouse must already exist
 * - Existing stock must be preserved
 * - New capacity must be able to accommodate current stock
 *
 * This use case is intentionally strict to protect inventory consistency.
 */
@ApplicationScoped
public class ReplaceWarehouseUseCase implements ReplaceWarehouseOperation {

    private static final Logger LOG = Logger.getLogger(ReplaceWarehouseUseCase.class);

    private final WarehouseStore warehouseStore;

    public ReplaceWarehouseUseCase(WarehouseStore warehouseStore) {
        this.warehouseStore = warehouseStore;
    }

    @Inject
    ArchiveWarehouseOperation archiveWarehouseOperation;

    /**
     * Replaces an existing warehouse with a new definition.
     * <p>
     * Transactional to ensure atomic validation and update.
     */
    @Transactional
    @Override
    public void replace(Warehouse newWarehouse) {

        LOG.debugf(
                "Attempting to replace warehouse with businessUnitCode=%s",
                newWarehouse.businessUnitCode
        );

        // Load existing warehouse state

        Warehouse existing =
                warehouseStore.findByBusinessUnitCode(
                        newWarehouse.businessUnitCode);

        // Business invariant: warehouse must already exist
        if (existing == null) {
            LOG.warnf(
                    "Replace failed. Warehouse not found with businessUnitCode=%s",
                    newWarehouse.businessUnitCode
            );
            throw new IllegalArgumentException(
                    "Warehouse not found: " + newWarehouse.businessUnitCode
            );
        }


        // Capacity accommodation rule:
        // New warehouse capacity must be sufficient for existing stock
        if (newWarehouse.capacity < existing.stock) {
            LOG.warnf(
                    "Replace failed. New capacity %d cannot accommodate existing stock %d for businessUnitCode=%s",
                    newWarehouse.capacity,
                    existing.stock,
                    newWarehouse.businessUnitCode
            );
            throw new IllegalArgumentException(
                    "New warehouse capacity cannot accommodate existing stock");
        }

        // Stock consistency rule:
        // Replacement must not change the current stock value
        if (!newWarehouse.stock.equals(existing.stock)) {
            LOG.warnf(
                    "Replace failed. Stock mismatch for businessUnitCode=%s. Existing=%d, New=%d",
                    newWarehouse.businessUnitCode,
                    existing.stock,
                    newWarehouse.stock
            );
            throw new IllegalArgumentException(
                    "Stock of new warehouse must match existing warehouse");
        }


//archive the existing warehouse
        LOG.info("archiving the existing warehouse to be replaced");
        archiveWarehouseOperation.archive(existing);

        // Persist replacement
        warehouseStore.update(newWarehouse);

        LOG.infof(
                "Warehouse replaced successfully with businessUnitCode=%s",
                newWarehouse.businessUnitCode
        );
    }
}