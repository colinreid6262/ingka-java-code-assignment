package com.fulfilment.application.monolith.stores;

import io.quarkus.hibernate.orm.panache.Panache;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class StoreResourceTest {

    @Mock
    LegacyStoreManagerGateway legacyStoreManagerGateway;

    @InjectMocks
    StoreResource target;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // The below tests do not provide complete comprehensive coverage of the target class, but are provided as an example of how I would approach the testing
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    void createWithId() {
        Store store = new Store();
        store.id = 1L;
        assertThrows(
                WebApplicationException.class,
                () -> target.create(store)
        );
    }

    @Test
    void create() {
        Store store = mock();
        store.name = "test";

        try (MockedStatic<Panache> panacheMock = mockStatic(Panache.class)) {
            panacheMock.when(Panache::getEntityManager).thenReturn(mock(EntityManager.class));

            Response response = target.create(store);
            verify(store).persist();
            verify(legacyStoreManagerGateway).createStoreOnLegacySystem(any());

            assertEquals(201, response.getStatus());
        }
    }

}