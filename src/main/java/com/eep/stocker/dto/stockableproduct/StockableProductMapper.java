package com.eep.stocker.dto.stockableproduct;

import com.eep.stocker.domain.*;
import com.eep.stocker.dto.MapperUtils;
import com.eep.stocker.dto.stockableproduct.composite.*;
import com.eep.stocker.dto.stockableproductnote.GetStockableProductNoteLowDetailResponse;
import com.eep.stocker.dto.stockableproductnote.StockableProductNoteMapper;
import com.eep.stocker.dto.stocktransaction.GetStockTransactionLowDetailResponse;
import com.eep.stocker.dto.supplier.SupplierMapper;
import com.eep.stocker.dto.supplierquote.SupplierQuoteMapper;
import com.eep.stocker.services.StockableProductService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = { MapperUtils.class, SupplierMapper.class, SupplierQuoteMapper.class, StockableProductNoteMapper.class })
public interface StockableProductMapper {
    CreateStockableProductRequest mapToCreateRequest(StockableProduct stockableProduct);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uid", ignore = true)
    StockableProduct mapFromCreateRequest(CreateStockableProductRequest request);

    @Mapping(target = "id", source = "product.uid")
    @Mapping(target = "onOrder", constant = "0.0")
    CreateStockableProductResponse mapToCreateResponse(StockableProduct product);

    @Mapping(target = "id", source = "product.uid")
    @Mapping(target = "onOrder", source = "onOrder", defaultValue = "0.0")
    GetStockableProductResponse mapToGetResponse(StockableProduct product, Double onOrder);

    @Mapping(target = "id", source = "product.uid")
    @Mapping(target = "onOrder", source = "onOrder", defaultValue = "0.0")
    UpdateStockableProductResponse mapToUpdateResponse(StockableProduct product, Double onOrder);

    @Mapping(target = "id", source = "product.uid")
    @Mapping(target = "onOrder", source = "onOrder", defaultValue = "0.0")
    @Mapping(target = "stockTransactions", source = "transactions")
    GetFullDetailStockableProductResponse mapToFullUpdateResponse(StockableProduct product,
                                                                  Double onOrder,
                                                                  List<StockableProductPurchaseOrderCompositeDTO> purchaseOrders,
                                                                  List<StockableProductDeliveryCompositeDTO> deliveries,
                                                                  List<GetStockTransactionLowDetailResponse> transactions,
                                                                  List<StockableProductSupplierQuoteCompositeDTO> quotes,
                                                                  List<GetStockableProductNoteLowDetailResponse> notes);

    @Mapping(target = "id", source = "uid")
    StockableProductPurchaseOrderCompositeDTO mapPurchaseOrderToComposite(PurchaseOrder order);

    @Mapping(target = "id", source = "delivery.uid")
    StockableProductDeliveryCompositeDTO mapDeliveryToComposite(Delivery delivery,
                                                                List<StockableProductDeliveryLineCompositeDTO> deliveryLines);

    @Mapping(target = "id", source = "uid")
    @Mapping(target = "stockTransactionId", source = "stockTransaction.uid")
    @Mapping(target = "deliveryId", source = "delivery.uid")
    StockableProductDeliveryLineCompositeDTO mapDeliveryLineToComposite(DeliveryLine deliveryLine);

    @Mapping(target = "id", source = "uid")
    @Mapping(target = "stockableProductId", source = "stockableProduct.uid")
    @Mapping(target = "purchaseOrderId", source = "purchaseOrder.uid")
    StockableProductPurchaseOrderLineCompositeDTO mapToOrderLineComposite(PurchaseOrderLine orderLine);

    @Mapping(target = "id", source = "uid")
    @Mapping(target = "stockableProductId", source = "stockableProduct.uid")
    StockableProductSupplierQuoteCompositeDTO mapToSupplierQuoteComposite(SupplierQuote supplierQuote);

    void updateFromUpdateRequest(@MappingTarget StockableProduct stockableProduct,
                                 UpdateStockableProductRequest request);
}
