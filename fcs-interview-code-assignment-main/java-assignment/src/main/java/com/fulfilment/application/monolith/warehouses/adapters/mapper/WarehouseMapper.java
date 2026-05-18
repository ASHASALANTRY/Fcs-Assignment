package com.fulfilment.application.monolith.warehouses.adapters.mapper;

import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.warehouse.api.beans.Warehouse;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.ZoneId;

@ApplicationScoped
public class WarehouseMapper {
    public com.fulfilment.application.monolith.warehouses.domain.models.Warehouse
    toDomain(Warehouse api) {

        var domain =
                new com.fulfilment.application.monolith.warehouses.domain.models.Warehouse();

        domain.businessUnitCode = api.getBusinessUnitCode();
        domain.location = api.getLocation();
        domain.capacity = api.getCapacity();
        domain.stock = api.getStock();

        return domain;
    }
    public Warehouse
    domainToApi(com.fulfilment.application.monolith.warehouses.domain.models.Warehouse domain) {

        var api = new Warehouse();
        api.setBusinessUnitCode(domain.businessUnitCode);
        api.setLocation(domain.location);
        api.setCapacity(domain.capacity);
        api.setStock(domain.stock);

        return api;
    }
    public Warehouse toApi(DbWarehouse db) {
        Warehouse api = new Warehouse();

        api.setId(String.valueOf(db.id));
        api.setBusinessUnitCode(db.businessUnitCode);
        api.setLocation(db.location);
        api.setCapacity(db.capacity);
        api.setStock(db.stock);

        return api;
    }
    public com.fulfilment.application.monolith.warehouses.domain.models.Warehouse
    toDomain(DbWarehouse db) {

        var domain = new com.fulfilment.application.monolith.warehouses.domain.models.Warehouse();
        domain.businessUnitCode = db.businessUnitCode;
        domain.location = db.location;
        domain.capacity = db.capacity;
        domain.stock = db.stock;
        domain.archivedAt = db.archivedAt != null
                ? db.archivedAt
                : null;

        return domain;
    }
}
