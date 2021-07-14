package com.eep.stocker.repository;

import com.eep.stocker.domain.*;
import com.eep.stocker.testdata.SupplierTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
class IDeliveryLineRepositoryTest extends SupplierTestData {

    @Autowired
    private IDeliveryLineRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private PurchaseOrder po1;
    private PurchaseOrder po2;
    private PurchaseOrderLine poLine1;
    private PurchaseOrderLine unsavedPoLine1;
    private PurchaseOrderLine poLine2;
    private StockTransaction transaction1;
    private StockTransaction transaction2;
    private StockTransaction transaction3;
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
    private void setUp() {

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

        mf220 = StockableProduct.builder()
                .name("MF220")
                .mpn("EEP200919001")
                .description("Mild Steel Flange")
                .category("Flanges")
                .units("Flanges")
                .stockPrice(1.72)
                .inStock(25.0)
                .build();

        MF286 = StockableProduct.builder()
                .name("MF286")
                .mpn("EEP200919002")
                .description("Mild Steel Flange")
                .category("Flanges")
                .units("Flanges")
                .stockPrice(1.45)
                .inStock(75.0)
                .build();

        //stock transactions
        transaction1 = new StockTransaction(mf220,
                15.0D, "Delivery No. " + "12345",
                "Test stock transaction", LocalDate.now());

        transaction2 = new StockTransaction(mf220,
                10.D, "Delivery No. " + "12345",
                "Test stock transaction", LocalDate.now());

        transaction3 = new StockTransaction(MF286,
                100.0D, "Delivery No. " + "12345",
                "Test stock transaction", LocalDate.now());

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
        deliveryLine1.setStockTransaction(transaction1);

        deliveryLine2 = new DeliveryLine();
        deliveryLine2.setDelivery(delivery2);
        deliveryLine2.setPurchaseOrderLine(poLine1);
        deliveryLine2.setNote("A note");
        deliveryLine2.setQuantityDelivered(10.0D);
        deliveryLine2.setStockTransaction(transaction2);

        deliveryLine3 = new DeliveryLine();
        deliveryLine3.setDelivery(delivery1);
        deliveryLine3.setPurchaseOrderLine(poLine2);
        deliveryLine3.setNote("A note");
        deliveryLine3.setQuantityDelivered(100.0D);
        deliveryLine3.setStockTransaction(transaction3);

        shelleys = entityManager.persistFlushFind(shelleys);
        ukf = entityManager.persistFlushFind(ukf);
        po1 = entityManager.persistFlushFind(po1);
        po2 = entityManager.persistFlushFind(po2);
        mf220 = entityManager.persistFlushFind(mf220);
        MF286 = entityManager.persistFlushFind(MF286);
        transaction1 = entityManager.persistFlushFind(transaction1);
        transaction2 = entityManager.persistFlushFind(transaction2);
        transaction3 = entityManager.persistFlushFind(transaction3);
        poLine1 = entityManager.persistFlushFind(poLine1);
        poLine2 = entityManager.persistFlushFind(poLine2);
        delivery1 = entityManager.persistFlushFind(delivery1);
        delivery2 = entityManager.persistFlushFind(delivery2);
        delivery3 = entityManager.persistFlushFind(delivery3);
        deliveryLine1 = entityManager.persistFlushFind(deliveryLine1);
        deliveryLine2 = entityManager.persistFlushFind(deliveryLine2);
        deliveryLine3 = entityManager.persistFlushFind(deliveryLine3);
    }

    @Test
    void findAllTest() {

        List<DeliveryLine> allDeliverieLines = repository.findAll();

        assertThat(allDeliverieLines.size()).isEqualTo(3);
        assertThat(allDeliverieLines).contains(deliveryLine1);
        assertThat(allDeliverieLines).contains(deliveryLine2);
        assertThat(allDeliverieLines).contains(deliveryLine3);
    }

    @Test
    void findByIdTest() {
        Optional<DeliveryLine> deliveryLine = repository.findById(deliveryLine2.getId());

        assertThat(deliveryLine.isPresent()).isTrue();
        assertThat(deliveryLine.get()).isEqualTo(deliveryLine2);
    }

    @Test
    void findBySupplierTest() {
        List<DeliveryLine> shelleyDeliveryLines = repository.findAllByPurchaseOrderLine_PurchaseOrder_Supplier(shelleys);
        List<DeliveryLine> ukfDeliveryLines = repository.findAllByPurchaseOrderLine_PurchaseOrder_Supplier(ukf);

        assertThat(shelleyDeliveryLines.size()).isEqualTo(3);
        assertThat(shelleyDeliveryLines).contains(deliveryLine1);
        assertThat(shelleyDeliveryLines).contains(deliveryLine2);
        assertThat(shelleyDeliveryLines).contains(deliveryLine3);
        assertThat(ukfDeliveryLines.size()).isEqualTo(0);
    }

    @Test
    void findByProductTest() {
        List<DeliveryLine> deliveryLines = repository.findAllByPurchaseOrderLine_StockableProduct(mf220);

        assertThat(deliveryLines.size()).isEqualTo(2);
        assertThat(deliveryLines).contains(deliveryLine1);
        assertThat(deliveryLines).contains(deliveryLine2);
    }

    @Test
    void findByPurchaseOrderTest() {
        List<DeliveryLine> deliveryLines = repository.findAllByPurchaseOrderLine_PurchaseOrder(po1);
        List<DeliveryLine> po2DeliveryLines = repository.findAllByPurchaseOrderLine_PurchaseOrder(po2);

        assertThat(deliveryLines.size()).isEqualTo(3);
        assertThat(deliveryLines).contains(deliveryLine1);
        assertThat(deliveryLines).contains(deliveryLine2);
        assertThat(deliveryLines).contains(deliveryLine3);
        assertThat(po2DeliveryLines.size()).isEqualTo(0);
    }

    @Test
    void deleteDeliveryLineTest() {
        List<DeliveryLine> deliveryLines = repository.findAll();

        assertThat(deliveryLines.size()).isEqualTo(3);
        assertThat(deliveryLines).contains(deliveryLine1, deliveryLine2, deliveryLine3);

        repository.delete(deliveryLine2);

        List<DeliveryLine> deliveryLines2 = repository.findAll();
        assertThat(deliveryLines2.size()).isEqualTo(2);
        assertThat(deliveryLines2).contains(deliveryLine1, deliveryLine3);
    }

    @Test
    void updateDeliveryLineTest() {
        Long id = deliveryLine2.getId();

        assertThat(deliveryLine2.getNote()).isEqualTo("A note");

        deliveryLine2.setNote("An updated note");

        repository.save(deliveryLine2);

        Optional<DeliveryLine> deliveryLine = repository.findById(id);
        assertThat(deliveryLine.isPresent()).isTrue();
        assertThat(deliveryLine.get().getNote()).isEqualTo("An updated note");
    }
}