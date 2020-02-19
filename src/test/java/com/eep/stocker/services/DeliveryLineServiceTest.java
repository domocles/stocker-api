package com.eep.stocker.services;

import com.eep.stocker.domain.*;
import com.eep.stocker.repository.IDeliveryLineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

class DeliveryLineServiceTest {
    @Mock
    private IDeliveryLineRepository deliveryLineRepository;

    private DeliveryLineService deliveryLineService;

    private Supplier shelleys;
    private PurchaseOrder po1;
    private PurchaseOrder po2;
    private PurchaseOrderLine poLine1;
    private PurchaseOrderLine unsavedPoLine1;
    private PurchaseOrderLine poLine2;
    private StockableProduct mf220;
    private StockableProduct MF286;

    Supplier ukf;
    Delivery delivery1;
    Delivery delivery2;
    Delivery delivery3;

    private DeliveryLine deliveryLine1;
    private DeliveryLine deliveryLine2;
    private DeliveryLine deliveryLine3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        deliveryLineService = new DeliveryLineService(deliveryLineRepository);

        shelleys = new Supplier("Shelley Parts Ltd",
                "GBP",
                "jon.horton@shelleyparts.co.uk",
                "01527 584285");

        po1 = new PurchaseOrder();
        //po1.setId(1L);
        po1.setSupplier(shelleys);
        po1.setPurchaseOrderReference("PO-001");
        po1.setPurchaseOrderDate(new Date());

        po2 = new PurchaseOrder();
        //po2.setId(2L);
        po2.setSupplier(shelleys);
        po2.setPurchaseOrderReference("PO-002");
        po2.setPurchaseOrderDate(new Date());

        mf220 = new StockableProduct(null, "MF220",
                "EEP200919001",
                "Mild Steel Flange",
                "Flange",
                new HashSet<String>(),
                "Flanges",
                1.72D,
                25.0D);

        MF286 = new StockableProduct(null, "MF286",
                "EEP200919002",
                "Mild Steel Flange",
                "Flange",
                new HashSet<String>(),
                "Flanges",
                1.45D,
                75.D);

        unsavedPoLine1 = new PurchaseOrderLine();
        unsavedPoLine1.setNote("First purchase order line");
        unsavedPoLine1.setPurchaseOrder(po1);
        unsavedPoLine1.setStockableProduct(mf220);
        unsavedPoLine1.setQty(25.0D);
        unsavedPoLine1.setPrice(1.27D);

        poLine1 = new PurchaseOrderLine();
        //poLine1.setId(1L);
        poLine1.setNote("First purchase order line");
        poLine1.setPurchaseOrder(po1);
        poLine1.setStockableProduct(mf220);
        poLine1.setQty(25.0D);
        poLine1.setPrice(1.27D);

        poLine2 = new PurchaseOrderLine();
        //poLine2.setId(2L);
        poLine2.setNote("Second purchase order line");
        poLine2.setPurchaseOrder(po1);
        poLine2.setStockableProduct(MF286);
        poLine2.setQty(100.0D);
        poLine2.setPrice(1.35D);

        ukf = new Supplier();
        ukf.setSupplierName("UKF Ltd");
        ukf.setDefaultCurrency("GBP");
        ukf.setEmailAddress("sales@ukf-group.com");
        ukf.setTelephoneNumber("01527 578686");

        delivery1 = new Delivery();
        delivery1.setSupplier(shelleys);
        delivery1.setReference("12345");
        delivery1.setNote("Last minute delivery");

        delivery2 = new Delivery();
        delivery2.setSupplier(ukf);
        delivery2.setReference("12345");
        delivery2.setNote("Last minute delivery 2");

        delivery3 = new Delivery();
        delivery3.setSupplier(shelleys);
        delivery3.setReference("12345");
        delivery3.setNote("Last minute delivery 3");

        deliveryLine1 = new DeliveryLine();
        deliveryLine1.setDelivery(delivery1);
        deliveryLine1.setPurchaseOrderLine(poLine1);
        deliveryLine1.setNote("A note");
        deliveryLine1.setQuantityDelivered(15.0D);

        deliveryLine2 = new DeliveryLine();
        deliveryLine2.setDelivery(delivery2);
        deliveryLine2.setPurchaseOrderLine(poLine1);
        deliveryLine2.setNote("A note");
        deliveryLine2.setQuantityDelivered(10.0D);

        deliveryLine3 = new DeliveryLine();
        deliveryLine3.setDelivery(delivery1);
        deliveryLine3.setPurchaseOrderLine(poLine2);
        deliveryLine3.setNote("A note");
        deliveryLine3.setQuantityDelivered(100.0D);
    }

    @Test
    void getDeliveryLineByIdTest() {
        given(deliveryLineRepository.findById(anyLong())).willReturn(Optional.of(deliveryLine2));

        Optional<DeliveryLine> deliveryLine = deliveryLineService.getDeliveryLineById(2L);

        assertThat(deliveryLine.isPresent()).isTrue();
        assertThat(deliveryLine.get()).isEqualTo(deliveryLine2);
    }

    @Test
    void getAllDeliveryLinesTest() {
        given(deliveryLineRepository.findAll()).willReturn(Arrays.asList(deliveryLine1, deliveryLine2, deliveryLine3));

        List<DeliveryLine> deliveryLines = deliveryLineService.getAllDeliveryLines();

        assertThat(deliveryLines.size()).isEqualTo(3);
        assertThat(deliveryLines).contains(deliveryLine1, deliveryLine2, deliveryLine3);
    }

    @Test
    void getDeliveryLinesBySupplierTest() {
        given(deliveryLineRepository.findAllByPurchaseOrderLine_PurchaseOrder_Supplier(any(Supplier.class)))
                .willReturn(Arrays.asList(deliveryLine3, deliveryLine1));

        List<DeliveryLine> deliveryLines = deliveryLineService.getAllDeliveryLinesForSupplier(shelleys);

        assertThat(deliveryLines.size()).isEqualTo(2);
        assertThat(deliveryLines).contains(deliveryLine1, deliveryLine3);
    }

    @Test
    void getDeliveryLinesForStockableProductTest() {
        given(deliveryLineRepository.findAllByPurchaseOrderLine_StockableProduct(any(StockableProduct.class)))
                .willReturn(Arrays.asList(deliveryLine2, deliveryLine1));

        List<DeliveryLine> deliveryLines = deliveryLineService.getAllDeliveryLinesForStockableProduct(mf220);

        assertThat(deliveryLines.size()).isEqualTo(2);
        assertThat(deliveryLines).contains(deliveryLine1, deliveryLine2);
    }

    @Test
    void getDeliveryLinesForPurchaseOrderTest() {
        given(deliveryLineRepository.findAllByPurchaseOrderLine_PurchaseOrder(any(PurchaseOrder.class)))
                .willReturn(Arrays.asList(deliveryLine2, deliveryLine3));

        List<DeliveryLine> deliveryLines = deliveryLineService.getAllDeliveryLinesForPurchaseOrder(po1);

        assertThat(deliveryLines.size()).isEqualTo(2);
        assertThat(deliveryLines).contains(deliveryLine3, deliveryLine2);
    }

    @Test
    void saveDeliveryLinesTest() {
        DeliveryLine deliveryLine = new DeliveryLine();
        deliveryLine.setDelivery(delivery2);
        deliveryLine.setPurchaseOrderLine(poLine2);
        deliveryLine.setNote("A note");
        deliveryLine.setQuantityDelivered(12.0D);

        DeliveryLine savedDeliveryLine = new DeliveryLine();
        savedDeliveryLine.setId(5L);
        savedDeliveryLine.setDelivery(delivery2);
        savedDeliveryLine.setPurchaseOrderLine(poLine2);
        savedDeliveryLine.setNote("A note");
        savedDeliveryLine.setQuantityDelivered(12.0D);

        given(deliveryLineRepository.save(deliveryLine)).willReturn(savedDeliveryLine);

        DeliveryLine delLine = deliveryLineService.save(deliveryLine);

        assertThat(delLine).isEqualTo(savedDeliveryLine);
    }
}