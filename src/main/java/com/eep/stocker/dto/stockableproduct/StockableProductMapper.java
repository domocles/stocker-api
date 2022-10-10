package com.eep.stocker.dto.stockableproduct;

import com.eep.stocker.domain.*;
import com.eep.stocker.dto.MapperUtils;
import com.eep.stocker.dto.stockableproduct.composite.*;
import com.eep.stocker.dto.stockableproductnote.GetStockableProductNoteLowDetailResponse;
import com.eep.stocker.dto.stockableproductnote.StockableProductNoteMapper;
import com.eep.stocker.dto.stocktransaction.GetStockTransactionLowDetailResponse;
import com.eep.stocker.dto.stocktransaction.StockTransactionMapper;
import com.eep.stocker.dto.supplier.SupplierMapper;
import com.eep.stocker.dto.supplierquote.SupplierQuoteMapper;
import com.eep.stocker.services.StockableProductService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Mapper(componentModel = "spring", uses = { MapperUtils.class,
        SupplierMapper.class,
        SupplierQuoteMapper.class,
        StockableProductNoteMapper.class,
        StockTransactionMapper.class})
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
    @Mapping(target = "purchaseOrders", source = "orderLines")
    GetFullDetailStockableProductResponse mapToFullUpdateResponse(StockableProduct product,
                                                                  Double onOrder,
                                                                  List<PurchaseOrderLine> orderLines,
                                                                  List<DeliveryLine> deliveries,
                                                                  List<StockTransaction> transactions,
                                                                  List<SupplierQuote> quotes,
                                                                  List<StockableProductNote> notes);

    default List<StockableProductPurchaseOrderCompositeDTO> mapPurchaseOrderCompositeFromLines(List<PurchaseOrderLine> orderLines) {
        var purchaseOrders = orderLines.stream().collect(groupingBy(PurchaseOrderLine::getPurchaseOrder));
        var purchaseOrderComposites = purchaseOrders.entrySet().stream()
                .map(e -> mapPurchaseOrderToComposite(
                        e.getKey(),
                        e.getValue().stream()
                            .map(this::mapToOrderLineComposite)
                            .collect(Collectors.toList())))
                .collect(Collectors.toList());
        return purchaseOrderComposites;
    }

    default List<StockableProductDeliveryCompositeDTO> mapDeliveryCompositeFromLines(List<DeliveryLine> deliveryLines) {
        var deliveries = deliveryLines.stream().collect(groupingBy(DeliveryLine::getDelivery));
        var deliveryComposites = deliveries.entrySet().stream()
                .map(e -> mapDeliveryToComposite(
                        e.getKey(),
                        e.getValue().stream()
                                .map(this::mapDeliveryLineToComposite)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
        return deliveryComposites;
    }

    @Mapping(target = "id", source = "order.uid")
    StockableProductPurchaseOrderCompositeDTO mapPurchaseOrderToComposite(PurchaseOrder order, List<StockableProductPurchaseOrderLineCompositeDTO> orderLines);

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

    List<StockableProductSupplierQuoteCompositeDTO> mapToSupplierQuoteComposites(List<SupplierQuote> supplierQuote);

    void updateFromUpdateRequest(@MappingTarget StockableProduct stockableProduct,
                                 UpdateStockableProductRequest request);
}
