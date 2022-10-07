package com.eep.stocker.dto.stockableproduct;

import com.eep.stocker.domain.*;
import com.eep.stocker.dto.stockableproductnote.StockableProductNoteMapper;
import com.eep.stocker.dto.stocktransaction.StockTransactionMapper;
import com.eep.stocker.dto.supplier.SupplierMapper;
import com.eep.stocker.services.StockableProductService;
import com.eep.stocker.testdata.DeliveryTestData;
import com.eep.stocker.testdata.SupplierTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StockableProductMapperTest extends SupplierTestData {

    @Autowired
    StockableProductMapper stockableProductMapper;

    @Autowired
    StockableProductNoteMapper stockableProductNoteMapper;

    @Autowired
    SupplierMapper supplierMapper;

    @Autowired
    StockTransactionMapper transactionMapper;

    @MockBean
    private StockableProductService service;

    private PurchaseOrder po1;
    private PurchaseOrder po2;
    private PurchaseOrderLine poLine1;
    private PurchaseOrderLine unsavedPoLine1;
    private PurchaseOrderLine poLine2;

    private StockableProduct mf220;
    private StockableProduct MF286;

    private StockableProductNote mf220note;
    private StockableProductNote mf286note;

    Supplier ukf;
    Delivery delivery1;
    Delivery delivery2;
    Delivery delivery3;

    private DeliveryLine deliveryLine1;
    private DeliveryLine deliveryLine2;
    private DeliveryLine deliveryLine3;

    private SupplierQuote supplierQuote1;
    private SupplierQuote supplierQuote2;

    @BeforeEach
    void setUp() {
        po1 = new PurchaseOrder();
        po1.setId(1L);
        po1.setSupplier(shelleys);
        po1.setPurchaseOrderReference("PO-001");
        po1.setPurchaseOrderDate(LocalDate.now());

        po2 = new PurchaseOrder();
        po2.setId(2L);
        po2.setSupplier(shelleys);
        po2.setPurchaseOrderReference("PO-002");
        po2.setPurchaseOrderDate(LocalDate.now());

        mf220 = StockableProduct.builder()
                .id(1L)
                .name("MF220")
                .mpn("EEP200919001")
                .description("Mild Steel Flange")
                .category("Flanges")
                .units("Flanges")
                .stockPrice(1.72)
                .inStock(25.0)
                .build();

        MF286 = StockableProduct.builder()
                .id(2L)
                .name("MF286")
                .mpn("EEP200919002")
                .description("Mild Steel Flange")
                .category("Flanges")
                .units("Flanges")
                .stockPrice(1.45)
                .inStock(75.0)
                .build();

        mf220note = new StockableProductNote();
        mf220note.setStockableProduct(mf220);
        mf220note.setNote("A 50mm flange");

        mf286note = new StockableProductNote();
        mf286note.setStockableProduct(MF286);
        mf286note.setNote("A 60mm flange");

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
        deliveryLine1.setId(1L);
        deliveryLine1.setDelivery(delivery1);
        deliveryLine1.setPurchaseOrderLine(poLine1);
        deliveryLine1.setNote("A note");
        deliveryLine1.setQuantityDelivered(15.0D);
        StockTransaction transaction1 = new StockTransaction(poLine1.getStockableProduct(),
                deliveryLine1.getQuantityDelivered(), "Delivery No. " + delivery1.getReference(),
                "Test stock transaction");
        transaction1.setId(1L);
        deliveryLine1.setStockTransaction(transaction1);

        deliveryLine2 = new DeliveryLine();
        deliveryLine2.setId(2L);
        deliveryLine2.setDelivery(delivery2);
        deliveryLine2.setPurchaseOrderLine(poLine1);
        deliveryLine2.setNote("A note");
        deliveryLine2.setQuantityDelivered(10.0D);
        StockTransaction transaction2 = new StockTransaction(poLine1.getStockableProduct(),
                deliveryLine2.getQuantityDelivered(), "Delivery No. " + delivery2.getReference(),
                "Test stock transaction");
        transaction2.setId(2L);
        deliveryLine2.setStockTransaction(transaction2);

        deliveryLine3 = new DeliveryLine();
        deliveryLine3.setId(3L);
        deliveryLine3.setDelivery(delivery1);
        deliveryLine3.setPurchaseOrderLine(poLine2);
        deliveryLine3.setNote("A note");
        deliveryLine3.setQuantityDelivered(100.0D);
        StockTransaction transaction3 = new StockTransaction(poLine2.getStockableProduct(),
                deliveryLine3.getQuantityDelivered(), "Delivery No. " + delivery3.getReference(),
                "Test stock transaction");
        transaction3.setId(3L);
        deliveryLine3.setStockTransaction(transaction3);

        supplierQuote1 = new SupplierQuote();
        supplierQuote1.setStockableProduct(mf220);
        supplierQuote1.setSupplier(shelleys);
        supplierQuote1.setQuotationDate(LocalDate.of(2022, 6, 5));
        supplierQuote1.setPrice(1.21);
        supplierQuote1.setQty(15.0);

        supplierQuote2 = new SupplierQuote();
        supplierQuote2.setStockableProduct(mf220);
        supplierQuote2.setSupplier(ukf);
        supplierQuote2.setQuotationDate(LocalDate.of(2022, 8, 12));
        supplierQuote2.setPrice(1.45);
        supplierQuote2.setQty(25.0);
    }

    private StockableProduct stockableProduct = StockableProduct.builder()
            .id(1L)
            .name("51mm x 152mm Ilok Flex")
            .mpn("FX-51-152-I")
            .description("An ilok lined flex with a bore of 51mm and an overall length of 152mm.  " +
                    "Stainless steel collars.")
            .category("Flex")
            .tag("51mm")
            .units("Flexes")
            .stockPrice(1.0)
            .inStock(50.0)
            .build();

    private CreateStockableProductRequest request = CreateStockableProductRequest.builder()
            .name("51mm x 152mm Ilok Flex")
            .mpn("FX-51-152-I")
            .description("An ilok lined flex with a bore of 51mm and an overall length of 152mm.  " +
                    "Stainless steel collars.")
            .category("Flex")
            .tag("51mm")
            .units("Flexes")
            .stockPrice(BigDecimal.valueOf(1))
            .inStock(BigDecimal.valueOf(50.0))
            .build();

    private CreateStockableProductResponse response = CreateStockableProductResponse.builder()
            .name("51mm x 152mm Ilok Flex")
            .mpn("FX-51-152-I")
            .description("An ilok lined flex with a bore of 51mm and an overall length of 152mm.  " +
                    "Stainless steel collars.")
            .category("Flex")
            .tag("51mm")
            .units("Flexes")
            .stockPrice(BigDecimal.valueOf(1))
            .inStock(BigDecimal.valueOf(50.0))
            .build();

    private GetStockableProductResponse getResponse = GetStockableProductResponse.builder()
            .name("51mm x 152mm Ilok Flex")
            .mpn("FX-51-152-I")
            .description("An ilok lined flex with a bore of 51mm and an overall length of 152mm.  " +
                    "Stainless steel collars.")
            .category("Flex")
            .tag("51mm")
            .units("Flexes")
            .stockPrice(BigDecimal.valueOf(1))
            .inStock(BigDecimal.valueOf(50.0))
            .build();

    @Test
    void stockableProductToStockableProductRequest() {

        CreateStockableProductRequest req = stockableProductMapper.mapToCreateRequest(stockableProduct);
        assertThat(req.getName()).isEqualTo("51mm x 152mm Ilok Flex");
        assertThat(req.getMpn()).isEqualTo("FX-51-152-I");
        assertThat(req.getDescription()).isEqualTo("An ilok lined flex with a bore of 51mm and an overall length of 152mm.  " +
                "Stainless steel collars.");
        assertThat(req.getCategory()).isEqualTo("Flex");
        assertThat(req.getTags()).contains("51mm");
        assertThat(req.getStockPrice().doubleValue()).isEqualTo(1.0);
        assertThat(req.getInStock().doubleValue()).isEqualTo(50.0);
    }

    @Test
    void stockableProductFromCreateStockableProductRequest() {
        StockableProduct stockableProduct = stockableProductMapper.mapFromCreateRequest(request);
        assertThat(stockableProduct.getName()).isEqualTo("51mm x 152mm Ilok Flex");
        assertThat(stockableProduct.getUid().toString()).isNotEmpty();
        assertThat(stockableProduct.getMpn()).isEqualTo("FX-51-152-I");
        assertThat(stockableProduct.getDescription()).isEqualTo("An ilok lined flex with a bore of 51mm and an overall length of 152mm.  " +
                "Stainless steel collars.");
        assertThat(stockableProduct.getCategory()).isEqualTo("Flex");
        assertThat(stockableProduct.getTags()).contains("51mm");
        assertThat(stockableProduct.getStockPrice()).isEqualTo(1.0);
        assertThat(stockableProduct.getInStock()).isEqualTo(50.0);
    }

    @Test
    void createStockableProductResponseFromStockableProduct() {
        var res = stockableProductMapper.mapToCreateResponse(stockableProduct);
        assertThat(res.getName()).isEqualTo("51mm x 152mm Ilok Flex");
        assertThat(res.getId()).isNotEmpty();
        assertThat(res.getMpn()).isEqualTo("FX-51-152-I");
        assertThat(res.getDescription()).isEqualTo("An ilok lined flex with a bore of 51mm and an overall length of 152mm.  " +
                "Stainless steel collars.");
        assertThat(res.getCategory()).isEqualTo("Flex");
        assertThat(res.getTags()).contains("51mm");
        assertThat(res.getStockPrice()).isEqualTo(BigDecimal.valueOf(1.0));
        assertThat(res.getInStock()).isEqualTo(BigDecimal.valueOf(50.0));
    }

    @Test
    void createGetStockableProductResponseFromStockableProduct() {
        var res = stockableProductMapper.mapToGetResponse(stockableProduct, 0.0);
        assertThat(res.getName()).isEqualTo("51mm x 152mm Ilok Flex");
        assertThat(res.getId()).isNotEmpty();
        assertThat(res.getMpn()).isEqualTo("FX-51-152-I");
        assertThat(res.getDescription()).isEqualTo("An ilok lined flex with a bore of 51mm and an overall length of 152mm.  " +
                "Stainless steel collars.");
        assertAll(
                () -> assertThat(res.getCategory()).isEqualTo("Flex"),
                () -> assertThat(res.getTags()).contains("51mm"),
                () -> assertThat(res.getStockPrice()).isEqualTo(BigDecimal.valueOf(1.0)),
                () -> assertThat(res.getInStock()).isEqualTo(BigDecimal.valueOf(50.0)),
                () -> assertThat(res.getOnOrder()).isEqualTo(BigDecimal.valueOf(100.0))
        );
    }

    @Test
    void createFullDetailResponseTest() {
        var po = stockableProductMapper.mapPurchaseOrderToComposite(po1);
        var deliveryLine = stockableProductMapper.mapDeliveryLineToComposite(deliveryLine1);
        var delivery = stockableProductMapper.mapDeliveryToComposite(delivery1, List.of(deliveryLine));
        var transaction = transactionMapper.mapToLowDetailResponse(deliveryLine1.getStockTransaction());
        var supplierQuote = stockableProductMapper.mapToSupplierQuoteComposite(supplierQuote1);
        var note = stockableProductNoteMapper.mapToLowDetailResponse(mf220note);
        var res = stockableProductMapper.mapToFullUpdateResponse(stockableProduct,
                50.0,
                List.of(po),
                List.of(delivery),
                List.of(transaction),
                List.of(supplierQuote),
                List.of(note));
        assertAll(
                () -> assertThat(res).isNotNull(),
                () -> assertThat(res.getCategory()).isNotNull().isEqualTo(stockableProduct.getCategory()),
                () -> assertThat(res.getName()).isNotNull().isEqualTo(stockableProduct.getName()),
                () -> assertThat(res.getPurchaseOrders()).contains(po),
                () -> assertThat(res.getDeliveries()).contains(delivery),
                () -> assertThat(res.getStockTransactions()).contains(transaction),
                () -> assertThat(res.getQuotes()).contains(supplierQuote)
        );
    }

    @Test
    void createPurchaseOrderCompositeTest() {
        var po = stockableProductMapper.mapPurchaseOrderToComposite(po1);
        var supplier = supplierMapper.getSupplierResponseFromSupplier(po1.getSupplier());
        assertAll(
                () -> assertThat(po.getSupplier()).isEqualTo(supplier),
                () -> assertThat(po.getPurchaseOrderReference()).isEqualTo(po1.getPurchaseOrderReference()),
                () -> assertThat(po.getPurchaseOrderDate()).isEqualTo(po1.getPurchaseOrderDate()),
                () -> assertThat(po.getStatus()).isEqualTo(po1.getStatus()),
                () -> assertThat(po.getId()).isEqualTo(po1.getUid().toString())
        );
    }

    @Test
    void createDeliveryLineTestComposite() {
        var deliveryLine = stockableProductMapper.mapDeliveryLineToComposite(deliveryLine1);
        assertAll(
                () -> assertThat(deliveryLine.getId()).isEqualTo(deliveryLine1.getUid().toString()),
                () -> assertThat(deliveryLine.getDeliveryId()).isEqualTo(deliveryLine1.getDelivery().getUid().toString()),
                () -> assertThat(deliveryLine.getStockTransactionId()).isEqualTo(deliveryLine1.getStockTransaction().getUid().toString()),
                () -> assertThat(deliveryLine.getNote()).isEqualTo(deliveryLine1.getNote()),
                () -> assertThat(deliveryLine.getQuantityDelivered()).isEqualTo(deliveryLine1.getQuantityDelivered())
        );
    }

    @Test
    void mapToOrderLineCompositeTest() {
        var orderLine = stockableProductMapper.mapToOrderLineComposite(poLine1);
        assertAll(
                () -> assertThat(orderLine.getId()).isEqualTo(poLine1.getUid().toString()),
                () -> assertThat(orderLine.getStockableProductId()).isEqualTo(poLine1.getStockableProduct().getUid().toString()),
                () -> assertThat(orderLine.getPurchaseOrderId()).isEqualTo(poLine1.getPurchaseOrder().getUid().toString()),
                () -> assertThat(orderLine.getPrice()).isEqualTo(poLine1.getPrice()),
                () -> assertThat(orderLine.getBalance()).isEqualTo(poLine1.getBalance()),
                () -> assertThat(orderLine.getQty()).isEqualTo(poLine1.getQty()),
                () -> assertThat(orderLine.getNote()).isEqualTo(poLine1.getNote()),
                () -> assertThat(orderLine.getStatus()).isEqualTo(poLine1.getStatus())

        );
    }

    @Test
    void mapToSupplierQuoteCompositeTest() {
        var quote = stockableProductMapper.mapToSupplierQuoteComposite(supplierQuote1);
        var supplier = supplierMapper.getSupplierResponseFromSupplier(supplierQuote1.getSupplier());
        assertAll(
                () -> assertThat(quote.getStockableProductId()).isEqualTo(mf220.getUid().toString()),
                () -> assertThat(quote.getSupplier()).isEqualTo(supplier),
                () -> assertThat(quote.getQuotationDate()).isEqualTo(supplierQuote1.getQuotationDate()),
                () -> assertThat(quote.getQty()).isEqualTo(supplierQuote1.getQty()),
                () -> assertThat(quote.getPrice()).isEqualTo(supplierQuote1.getPrice())
        );
    }
}