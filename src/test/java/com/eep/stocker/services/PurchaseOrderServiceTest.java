package com.eep.stocker.services;

import com.eep.stocker.domain.PurchaseOrder;
import com.eep.stocker.repository.IPurchaseOrderRepository;
import com.eep.stocker.testdata.SupplierTestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class PurchaseOrderServiceTest extends SupplierTestData {

    @Mock
    private IPurchaseOrderRepository purchaseOrderRepository;

    private PurchaseOrderService purchaseOrderService;

    private PurchaseOrder po1;
    private PurchaseOrder po2;
    private LocalDate now;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.purchaseOrderService = new PurchaseOrderService(purchaseOrderRepository);

         now = LocalDate.now();

        po1 = new PurchaseOrder();
        po1.setPurchaseOrderDate(now);
        po1.setPurchaseOrderReference("PO1");
        po1.setSupplier(shelleys);
        po1.setId(1L);

        po1 = new PurchaseOrder();
        po1.setPurchaseOrderDate(now);
        po1.setPurchaseOrderReference("PO2");
        po1.setSupplier(shelleys);
        po1.setId(2L);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void getAllPurchaseOrdersTest() {
        given(purchaseOrderRepository.findAll()).willReturn(Arrays.asList(po1, po2));

        List<PurchaseOrder> orders = this.purchaseOrderService.getAllPurchaseOrders();

        assertAll(
                () -> assertThat(orders).isNotNull(),
                () -> assertThat(orders.size()).isPositive(),
                () -> assertThat(orders).contains(po1),
                () -> assertThat(orders).contains(po2)
        );
    }

    @Test
    void testGetPurchaseOrderByUidTest() {
        given(purchaseOrderRepository.findByUid(any(UUID.class))).willReturn(Optional.of(po1));

        var purchaseOrder = purchaseOrderService.getPurchaseOrderFromUid(UUID.randomUUID());

        assertAll(
                () -> assertTrue(purchaseOrder.isPresent()),
                () -> assertEquals(purchaseOrder.get(), po1)
        );
    }

    @Test
    void getAllPurchaseOrdersForSupplierTest() {
        given(purchaseOrderRepository.findAllBySupplier(shelleys)).willReturn(Arrays.asList(po1, po2));

        List<PurchaseOrder> orders = this.purchaseOrderService.getAllPurchaseOrdersForSupplier(shelleys);

        assertAll(
                () -> assertThat(orders).isNotNull(),
                () -> assertThat(orders).contains(po1)
        );
    }

    @Test
    void getAllPurchaseOrdersBetweenDateTest() {
        given(purchaseOrderRepository.findAllByPurchaseOrderDateBetween(any(LocalDate.class), any(LocalDate.class)))
                .willReturn(Arrays.asList(po1, po2));

        List<PurchaseOrder> orders = this.purchaseOrderService.getAllPurchaseOrdersBetween(now, now);
        assertAll(
                () -> assertThat(orders).isNotNull(),
                () -> assertThat(orders).contains(po1),
                () -> assertThat(orders).contains(po2)
        );
    }

    @Test
    void getPurchaseOrderBySupplierReferenceTest() {
        given(purchaseOrderRepository.findAllBySupplierOrderReference(anyString())).willReturn(Arrays.asList(po1, po2));

        var orders = this.purchaseOrderService.getAllPurchaseOrdersBySupplierReference("PO00001");

        assertAll(
                () -> assertThat(orders.size()).isEqualTo(2),
                () -> assertThat(orders).contains(po1, po2)
        );
    }

    @Test
    void getPurchaseOrderByReferenceTest() {
        given(purchaseOrderRepository.findByPurchaseOrderReference(anyString())).willReturn(Optional.of(po1));

        var order = this.purchaseOrderService.getPurchaseOrderByReference("PO2");

        assertAll(
                () -> assertThat(order).isPresent(),
                () -> assertThat(order).contains(po1)
        );
    }

    @Test
    void getPurchaseOrderByReferenceDoesNotExistTest() {
        given(purchaseOrderRepository.findByPurchaseOrderReference(anyString())).willReturn(Optional.empty());

        var order = this.purchaseOrderService.getPurchaseOrderByReference("PO2");

        assertAll(
                () -> assertThat(order).isEmpty()
        );
    }

    @Test
    void getPurchaseOrderFromIdTest() {
        given(purchaseOrderService.getPurchaseOrderFromId(any(Long.class))).willReturn(Optional.of(po1));

        Optional<PurchaseOrder> purchaseOrder = purchaseOrderService.getPurchaseOrderFromId(1L);

        assertThat(purchaseOrder).isPresent();
        assertThat(purchaseOrder).contains(po1);
    }
}