package com.eep.stocker.services;

import com.eep.stocker.domain.PurchaseOrder;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.repository.IPurchaseOrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringJUnit4ClassRunner.class)
class PurchaseOrderServiceTest {

    @Mock
    private IPurchaseOrderRepository purchaseOrderRepository;

    private PurchaseOrderService purchaseOrderService;

    private PurchaseOrder po1;
    private PurchaseOrder po2;
    private Supplier shelleys;
    private Date now;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.purchaseOrderService = new PurchaseOrderService(purchaseOrderRepository);

         now = new Date();

        shelleys = new Supplier("Shelley Parts Ltd",
                "GBP",
                "jon.horton@shelleyparts.co.uk",
                "01527 584285");
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

        assertThat(orders).isNotNull();
        assertThat(orders.size()).isGreaterThan(0);
        assertThat(orders).contains(po1);
        assertThat(orders).contains(po2);
    }

    @Test
    public void getAllPurchaseOrdersForSupplierTest() {
        given(purchaseOrderRepository.findAllBySupplier(shelleys)).willReturn(Arrays.asList(po1, po2));

        List<PurchaseOrder> orders = this.purchaseOrderService.getAllPurchaseOrdersForSupplier(shelleys);

        assertThat(orders).isNotNull();
        assertThat(orders).contains(po1);
    }

    @Test
    public void getAllPurchaseOrdersBetweenDateTest() {
        given(purchaseOrderRepository.findAllByPurchaseOrderDateBetween(any(Date.class), any(Date.class)))
                .willReturn(Arrays.asList(po1, po2));

        List<PurchaseOrder> orders = this.purchaseOrderService.getAllPurchaseOrdersBetween(now, now);

        assertThat(orders).isNotNull();
        assertThat(orders).contains(po1);
        assertThat(orders).contains(po2);
    }
}