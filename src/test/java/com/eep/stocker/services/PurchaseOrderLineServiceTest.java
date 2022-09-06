package com.eep.stocker.services;

import com.eep.stocker.domain.PurchaseOrder;
import com.eep.stocker.domain.PurchaseOrderLine;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.repository.IDeliveryLineRepository;
import com.eep.stocker.repository.IPurchaseOrderLineRepository;
import com.eep.stocker.testdata.SupplierTestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

class PurchaseOrderLineServiceTest extends SupplierTestData {
    @Mock
    private IPurchaseOrderLineRepository purchaseOrderLineRepository;

    @Mock
    private IDeliveryLineRepository deliveryLineRepository;

    private PurchaseOrderLineService purchaseOrderLineService;

    private PurchaseOrder po1;
    private PurchaseOrder po2;
    private PurchaseOrderLine poLine1;
    private PurchaseOrderLine unsavedPoLine1;
    private PurchaseOrderLine poLine2;
    private StockableProduct mf220;
    private StockableProduct MF286;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        purchaseOrderLineService = new PurchaseOrderLineService(purchaseOrderLineRepository, deliveryLineRepository);

        po1 = new PurchaseOrder();
        po1.setId(1L);
        po1.setSupplier(supplier);
        po1.setPurchaseOrderReference("PO-001");
        po1.setPurchaseOrderDate(LocalDate.now());

        po2 = new PurchaseOrder();
        po2.setId(2L);
        po2.setSupplier(supplier);
        po2.setPurchaseOrderReference("PO-002");
        po2.setPurchaseOrderDate(LocalDate.now());

        mf220 = StockableProduct.builder()
                .id(1L)
                .name("MF220")
                .mpn("EEP200919001")
                .description("Mild Steel Flange")
                .category("Flanges")
                .stockPrice(1.72)
                .inStock(25.0)
                .build();

        MF286 = StockableProduct.builder()
                .id(2L)
                .name("MF286")
                .mpn("EEP200919002")
                .description("Mild Steel Flange")
                .category("Flanges")
                .stockPrice(1.45)
                .inStock(75.0)
                .build();

        unsavedPoLine1 = new PurchaseOrderLine();
        unsavedPoLine1.setNote("First purchase order line");
        unsavedPoLine1.setPurchaseOrder(po1);
        unsavedPoLine1.setStockableProduct(mf220);
        unsavedPoLine1.setQty(25.0D);
        unsavedPoLine1.setPrice(1.27D);

        poLine1 = new PurchaseOrderLine();
        poLine1.setId(1L);
        poLine1.setNote("First purchase order line");
        poLine1.setPurchaseOrder(po1);
        poLine1.setStockableProduct(mf220);
        poLine1.setQty(25.0D);
        poLine1.setPrice(1.27D);

        poLine2 = new PurchaseOrderLine();
        poLine2.setId(2L);
        poLine2.setNote("Second purchase order line");
        poLine2.setPurchaseOrder(po1);
        poLine2.setStockableProduct(MF286);
        poLine2.setQty(100.0D);
        poLine2.setPrice(1.35D);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getPurchaseOrderLineByUidTest() {
        given(purchaseOrderLineRepository.findByUid(anyString())).willReturn(Optional.of(poLine1));

        var orderLine = purchaseOrderLineService.getPurchaseOrderLineByUid(poLine1.getUid().toString());

        assertAll(
                () -> assertThat(orderLine).contains(poLine1)
        );
    }

    @Test
    void getAllPurchaseOrderLines() {
        given(purchaseOrderLineRepository.findAll()).willReturn(Arrays.asList(poLine1, poLine2));

        assertThat(purchaseOrderLineService.getAllPurchaseOrderLines()).isNotNull();
        assertThat(purchaseOrderLineService.getAllPurchaseOrderLines()).contains(poLine1);
        assertThat(purchaseOrderLineService.getAllPurchaseOrderLines()).contains(poLine2);
    }

    @Test
    void getAllPurchaseOrderLinesForProductTest() {
        given(purchaseOrderLineRepository.findAllByStockableProduct(any(StockableProduct.class)))
                .willReturn(Arrays.asList(poLine1, poLine2));

        assertThat(purchaseOrderLineService.getAllPurchaseOrderLinesForProduct(MF286)).isNotNull();
        assertThat(purchaseOrderLineService.getAllPurchaseOrderLinesForProduct(MF286)).contains(poLine1);
        assertThat(purchaseOrderLineService.getAllPurchaseOrderLinesForProduct(MF286)).contains(poLine2);
    }

    @Test
    void getAllPurchaseOrderLinesForPurchaseOrderTest() {
        given(purchaseOrderLineRepository.findAllByPurchaseOrder(any(PurchaseOrder.class)))
                .willReturn(Arrays.asList(poLine1, poLine2));

        assertThat(purchaseOrderLineService.getAllPurchaseOrderLinesForPurchaseOrder(po1)).isNotNull();
        assertThat(purchaseOrderLineService.getAllPurchaseOrderLinesForPurchaseOrder(po1)).contains(poLine1);
        assertThat(purchaseOrderLineService.getAllPurchaseOrderLinesForPurchaseOrder(po1)).contains(poLine2);
    }

    @Test
    void savePurchaseOrderLineTest() {
        given(purchaseOrderLineRepository.save(unsavedPoLine1)).willReturn(poLine1);

        PurchaseOrderLine poLine = purchaseOrderLineService.savePurchaseOrderLine(unsavedPoLine1);

        assertThat(poLine).isNotNull();
        assertThat(unsavedPoLine1.getId()).isNull();
        assertThat(poLine.getId()).isNotNull();
    }
}