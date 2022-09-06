package com.eep.stocker.dto.purchaseorderline;

import com.eep.stocker.domain.PurchaseOrder;
import com.eep.stocker.domain.PurchaseOrderLine;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.dto.MapperUtils;
import com.eep.stocker.dto.purchaseorder.PurchaseOrderMapper;
import com.eep.stocker.dto.stockableproduct.StockableProductMapper;
import com.eep.stocker.testdata.SupplierTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
public class PurchaseOrderLineMapperTest extends SupplierTestData {
    @Autowired
    PurchaseOrderLineMapper mapper;
    @Autowired
    PurchaseOrderMapper poMapper;
    @Autowired
    StockableProductMapper spMapper;


    private PurchaseOrder po1;
    private PurchaseOrder po2;
    private PurchaseOrderLine poLine1;
    private PurchaseOrderLine unsavedPoLine1;
    private PurchaseOrderLine poLine2;
    private StockableProduct mf220;
    private StockableProduct MF286;

    @BeforeEach
    public void setup() {
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

        poLine1 = new PurchaseOrderLine();
        poLine1.setId(1L);
        poLine1.setNote("First purchase order line");
        poLine1.setPurchaseOrder(po1);
        poLine1.setStockableProduct(mf220);
        poLine1.setQty(25.0D);
        poLine1.setPrice(1.27D);

        unsavedPoLine1 = new PurchaseOrderLine();
        unsavedPoLine1.setNote("First purchase order line");
        unsavedPoLine1.setPurchaseOrder(po1);
        unsavedPoLine1.setStockableProduct(mf220);
        unsavedPoLine1.setQty(25.0D);
        unsavedPoLine1.setPrice(1.27D);

        poLine2 = new PurchaseOrderLine();
        poLine2.setId(2L);
        poLine2.setNote("Second purchase order line");
        poLine2.setPurchaseOrder(po1);
        poLine2.setStockableProduct(MF286);
        poLine2.setQty(100.0D);
        poLine2.setPrice(1.35D);
    }

    @Test
    void mapperNotNullTest() {
        assertThat(mapper).isNotNull();
    }

    @Test
    void mapperMapsToGetResponseTest() {
        var mappedValue = mapper.mapToGetResponse(poLine1);
        var testMappedValue = GetPurchaseOrderLineResponse.builder()
                .balance(poLine1.getBalance())
                .purchaseOrder(poMapper.mapGetResponse(poLine1.getPurchaseOrder()))
                .id(poLine1.getUid().toString())
                .note(poLine1.getNote())
                .qty(poLine1.getQty())
                .status(poLine1.getStatus())
                .stockableProduct(spMapper.stockableProductResponseFromStockableProduct(poLine1.getStockableProduct()))
                .build();

        assertThat(testMappedValue).isEqualTo(mappedValue);
    }

    @Test
    void mapperMapsToGetLowDetailResponseTest() {
        var mappedValue = mapper.mapToGetLowDetailResponse(poLine1);
        var testMappedValue = GetPurchaseOrderLineLowDetailResponse.builder()
                .balance(poLine1.getBalance())
                .purchaseOrderId(poLine1.getPurchaseOrder().getUid().toString())
                .id(poLine1.getUid().toString())
                .note(poLine1.getNote())
                .qty(poLine1.getQty())
                .status(poLine1.getStatus())
                .stockableProductId(poLine1.getStockableProduct().getUid().toString())
                .build();

        assertThat(testMappedValue).isEqualTo(mappedValue);
    }
}
