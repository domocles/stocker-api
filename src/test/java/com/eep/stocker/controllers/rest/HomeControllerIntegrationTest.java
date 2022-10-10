package com.eep.stocker.controllers.rest;

import com.eep.stocker.domain.*;
import com.eep.stocker.dto.stockableproduct.*;
import com.eep.stocker.dto.stockableproductnote.StockableProductNoteMapper;
import com.eep.stocker.dto.stocktransaction.StockTransactionMapper;
import com.eep.stocker.services.*;
import com.eep.stocker.testdata.SupplierTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HomeControllerIntegrationTest extends SupplierTestData {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StockableProductMapper stockableProductMapper;

    @Autowired
    private StockTransactionMapper transactionMapper;

    @Autowired
    private StockableProductNoteMapper noteMapper;

    @MockBean
    private StockableProductService productService;

    @MockBean
    private StockableProductNoteService stockableProductNoteService;

    @MockBean
    private PurchaseOrderLineService orderLineService;

    @MockBean
    private DeliveryLineService deliveryLineService;

    @MockBean
    private StockTransactionService transactionService;

    @MockBean
    private SupplierQuoteService supplierQuoteService;


    private StockableProduct MF220;
    private StockableProduct MF286;

    private PurchaseOrder po1;
    private PurchaseOrder po2;
    private PurchaseOrderLine poLine1;
    private PurchaseOrderLine unsavedPoLine1;
    private PurchaseOrderLine poLine2;

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

    private UpdateStockableProductRequest updateStockableProductRequest;
    private UpdateStockableProductResponse updateStockableProductResponse;

    @BeforeEach
    public void setUp() {
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

        MF220 = StockableProduct.builder()
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

        updateStockableProductRequest = UpdateStockableProductRequest.builder()
                .name("MF220")
                .mpn("EEP200919005")
                .description("Mild Steel Flange")
                .category("Flanges")
                .units("Flanges")
                .stockPrice(BigDecimal.valueOf(1.72))
                .inStock(BigDecimal.valueOf(25.0))
                .build();

        updateStockableProductResponse = UpdateStockableProductResponse.builder()
                .id(MF220.getUid().toString())
                .name("MF220")
                .mpn("EEP200919005")
                .description("Mild Steel Flange")
                .category("Flanges")
                .units("Flanges")
                .stockPrice(BigDecimal.valueOf(1.72))
                .inStock(BigDecimal.valueOf(25.0))
                .build();

        mf220note = new StockableProductNote();
        mf220note.setStockableProduct(MF220);
        mf220note.setNote("A 50mm flange");

        mf286note = new StockableProductNote();
        mf286note.setStockableProduct(MF286);
        mf286note.setNote("A 60mm flange");

        unsavedPoLine1 = new PurchaseOrderLine();
        unsavedPoLine1.setNote("First purchase order line");
        unsavedPoLine1.setPurchaseOrder(po1);
        unsavedPoLine1.setStockableProduct(MF220);
        unsavedPoLine1.setQty(25.0D);
        unsavedPoLine1.setPrice(1.27D);

        poLine1 = new PurchaseOrderLine();
        poLine1.setId(1L);
        poLine1.setNote("First purchase order line");
        poLine1.setPurchaseOrder(po1);
        poLine1.setStockableProduct(MF220);
        poLine1.setQty(25.0D);
        poLine1.setPrice(1.27D);

        poLine2 = new PurchaseOrderLine();
        poLine2.setId(2L);
        poLine2.setNote("Second purchase order line");
        poLine2.setPurchaseOrder(po1);
        poLine2.setStockableProduct(MF286);
        poLine2.setQty(100.0D);
        poLine2.setPrice(1.35D);

        /*ukf = new Supplier();
        ukf.setSupplierName("UKF Ltd");
        ukf.setDefaultCurrency("GBP");
        ukf.setEmailAddress("sales@ukf-group.com");
        ukf.setTelephoneNumber("01527 578686");*/

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
        supplierQuote1.setStockableProduct(MF220);
        supplierQuote1.setSupplier(shelleys);
        supplierQuote1.setQuotationDate(LocalDate.of(2022, 6, 5));
        supplierQuote1.setPrice(1.21);
        supplierQuote1.setQty(15.0);

        supplierQuote2 = new SupplierQuote();
        supplierQuote2.setStockableProduct(MF220);
        supplierQuote2.setSupplier(ukf);
        supplierQuote2.setQuotationDate(LocalDate.of(2022, 8, 12));
        supplierQuote2.setPrice(1.45);
        supplierQuote2.setQty(25.0);
    }

    @Test
    void getStockableProductByUidTest() {
        given(productService.getStockableProductByUid(anyString())).willReturn(Optional.of(MF220));
        given(orderLineService.getSumOfOrdersForStockableProduct(any(StockableProduct.class)))
                .willReturn(Optional.of(100.0));
        given(deliveryLineService.getSumOfDeliveryLinesForStockableProduct(any(StockableProduct.class)))
                .willReturn(Optional.of(25.0));

        var response = restTemplate.exchange(
                "/api/stockable-products/" + MF220.getUid().toString(),
                HttpMethod.GET,
                null,
                GetStockableProductResponse.class
        );

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody().getOnOrder()).isNotNull().isEqualTo(BigDecimal.valueOf(75.0))
        );

    }

    @Test
    void getFullStockableProductTest() {
        given(productService.getStockableProductByUid(anyString())).willReturn(Optional.of(MF220));
        given(orderLineService.getAllPurchaseOrderLinesForProduct(any(StockableProduct.class)))
                .willReturn(List.of(poLine1));
        given(orderLineService.getSumOfOrdersForStockableProduct(any(StockableProduct.class)))
                .willReturn(Optional.of(100.0));
        given(deliveryLineService.getSumOfDeliveryLinesForStockableProduct(any(StockableProduct.class)))
                .willReturn(Optional.of(50.0));
        given(deliveryLineService.getAllDeliveryLinesForStockableProduct(any(StockableProduct.class)))
                .willReturn(List.of(deliveryLine1));
        given(transactionService.getAllStockTransactionsForStockableProduct(any(StockableProduct.class)))
                .willReturn(List.of(deliveryLine1.getStockTransaction()));
        given(supplierQuoteService.getAllSupplierQuotesForStockableProduct(any(StockableProduct.class)))
                .willReturn(List.of(supplierQuote1));
        given(stockableProductNoteService.getAllNotesForStockableProductUid(anyString()))
                .willReturn(List.of(mf220note));

        var po = stockableProductMapper.mapPurchaseOrderCompositeFromLines(List.of(poLine1));
        var deliveryLine = stockableProductMapper.mapDeliveryLineToComposite(deliveryLine1);
        var delivery = stockableProductMapper.mapDeliveryToComposite(delivery1, List.of(deliveryLine));
        var transaction = transactionMapper.mapToLowDetailResponse(deliveryLine1.getStockTransaction());
        var supplierQuote = stockableProductMapper.mapToSupplierQuoteComposite(supplierQuote1);
        var note = noteMapper.mapToLowDetailResponse(mf220note);

        var response = restTemplate.exchange(
                "/api/stockable-products/full/" + MF220.getUid().toString(),
                HttpMethod.GET,
                null,
                GetFullDetailStockableProductResponse.class
        );

        var testResponse = GetFullDetailStockableProductResponse.builder()
                .id(MF220.getUid().toString())
                .name(MF220.getName())
                .mpn(MF220.getMpn())
                .description(MF220.getDescription())
                .category(MF220.getCategory())
                .units(MF220.getUnits())
                .tags(MF220.getTags())
                .stockPrice(BigDecimal.valueOf(MF220.getStockPrice()))
                .inStock(BigDecimal.valueOf(MF220.getInStock()))
                .onOrder(BigDecimal.valueOf(50.0))
                .purchaseOrder(po.get(0))
                .delivery(delivery)
                .stockTransaction(transaction)
                .quote(supplierQuote)
                .note(note)
                .build();


        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotNull().isEqualTo(testResponse)
        );
    }

    @Test
    void updateStockableProductTest() {
        given(productService.getStockableProductByUid(anyString())).willReturn(Optional.of(MF220));
        given(productService.findStockableProductByMpn(anyString())).willReturn(Optional.empty());
        given(productService.updateStockableProduct(any(StockableProduct.class))).willReturn(MF220);

        UpdateStockableProductRequest req = updateStockableProductRequest;

        ResponseEntity<UpdateStockableProductResponse> res = restTemplate.exchange(
                "/api/stockable-products/update/" + MF220.getUid().toString(),
                HttpMethod.PUT,
                new HttpEntity<>(updateStockableProductRequest),
                UpdateStockableProductResponse.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isNotNull();
        assertThat(res.getBody()).isEqualTo(updateStockableProductResponse);
    }

    @Test
    void updateMpnWithClashThrowsMpnAlreadyExistsTest() {
        given(productService.getStockableProductByUid(anyString())).willReturn(Optional.of(MF220));
        given(productService.findStockableProductByMpn(anyString())).willReturn(Optional.of(MF286));

        UpdateStockableProductRequest req = updateStockableProductRequest;

        ResponseEntity<UpdateStockableProductResponse> res = restTemplate.exchange(
                "/api/stockable-products/update/" + MF220.getUid().toString(),
                HttpMethod.PUT,
                new HttpEntity<>(updateStockableProductRequest),
                UpdateStockableProductResponse.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
}
