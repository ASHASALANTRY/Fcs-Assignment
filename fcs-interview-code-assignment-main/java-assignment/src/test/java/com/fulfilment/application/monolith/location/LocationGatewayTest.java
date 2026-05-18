package com.fulfilment.application.monolith.location;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
@QuarkusTest
public class LocationGatewayTest {

    @Test
    public void testWhenResolveExistingLocationShouldReturn() {

        LocationGateway locationGateway = new LocationGateway();


        Location location = locationGateway.resolveByIdentifier("ZWOLLE-001");


        assertEquals(location.identification, "ZWOLLE-001");
    }
}
