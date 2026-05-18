package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@ApplicationScoped
public class WarehouseRepository implements WarehouseStore, PanacheRepository<DbWarehouse> {

    private static final Logger LOG = Logger.getLogger(WarehouseRepository.class);

    /**
     * Persists a new warehouse entity.
     */
    @Override
    public void create(Warehouse warehouse) {
        LOG.debugf("Creating warehouse with businessUnitCode={}", warehouse.businessUnitCode);

        DbWarehouse dbWarehouse=toDb(warehouse);
        dbWarehouse.createdAt = LocalDateTime.now();
        persist(dbWarehouse);
        LOG.infof("Warehouse created successfully with businessUnitCode=%s", warehouse.businessUnitCode);

    }

    /**
     * Updates an existing warehouse.
     *
     * Throws {@link IllegalStateException} if the warehouse does not exist.
     */
    @Override
    public void update(Warehouse warehouse) {
        LOG.debugf("Updating warehouse with businessUnitCode=%s", warehouse.businessUnitCode);
        DbWarehouse dbWarehouse=find("businessUnitCode", warehouse.businessUnitCode).firstResult();
        if (Objects.isNull(dbWarehouse))
            throw new IllegalStateException("Warehouse not found");

        // Update mutable fields only
        dbWarehouse.capacity = warehouse.capacity;
        dbWarehouse.stock = warehouse.stock;
        dbWarehouse.archivedAt = warehouse.archivedAt != null
                ? warehouse.archivedAt
                : null;
        LOG.infof("Warehouse updated successfully with businessUnitCode=%s", warehouse.businessUnitCode);
    }
    /**
     * Deletes a warehouse by business unit code.
     */
    @Override
    public void remove(Warehouse warehouse) {
        LOG.debugf("Deleting warehouse with businessUnitCode=%s", warehouse.businessUnitCode);

        delete("businessUnitCode", warehouse.businessUnitCode);

        LOG.infof("Warehouse deleted with businessUnitCode=%s", warehouse.businessUnitCode);
    }
    /**
     * Finds a warehouse by business unit code.
     *
     * @return Warehouse domain object or null if not found
     */
    @Override
    public Warehouse findByBusinessUnitCode(String buCode) {
        LOG.debugf("Fetching warehouse with businessUnitCode=%s", buCode);

        DbWarehouse dbWarehouse=find("businessUnitCode", buCode).firstResult();
        return Objects.isNull(dbWarehouse)? null:toDomain(dbWarehouse);
    }
    /**
     * Counts all active (non-archived) warehouses in a given location.
     */
    @Override
    public long countActiveByLocation(String location) {
        LOG.debugf("Counting active warehouses for location=%s", location);
        return count(
                "location = ?1 and archivedAt is null",
                location
        );
    }
    /**
     * Retrieves all active (non-archived) warehouses.
     */
    @Override
    public List<Warehouse> getAll() {
        LOG.debug("Fetching all active warehouses");

        return list("archivedAt is null").
                stream()
                .map(this::toDomain)
                .toList();
    }
    @Override
    public Warehouse findByBusinessUnitCodeAndisArchived(String buCode){
        LOG.debugf("Fetching warehouse with businessUnitCode=%s", buCode);
        DbWarehouse warehouse=find("businessUnitCode=?1 and archivedAt is null",buCode).firstResult();
        return toDomain(warehouse);
    }
    /**
     * Maps domain Warehouse to database entity.
     *
     * Keeps mapping logic isolated from business logic.
     */
    private DbWarehouse toDb(Warehouse w) {
        DbWarehouse db = new DbWarehouse();
        db.businessUnitCode = w.businessUnitCode;
        db.location = w.location;
        db.capacity = w.capacity;
        db.stock = w.stock;
        return db;
    }
    /**
     * Maps database entity to domain Warehouse.
     *
     * Converts LocalDateTime to ZonedDateTime (UTC) for domain consistency.
     */
    private Warehouse toDomain(DbWarehouse db) {
        Warehouse w = new Warehouse();
        w.businessUnitCode = db.businessUnitCode;
        w.location = db.location;
        w.capacity = db.capacity;
        w.stock = db.stock;
        if (db.archivedAt != null) {
            w.archivedAt = db.archivedAt;
        }
        return w;
    }
}
