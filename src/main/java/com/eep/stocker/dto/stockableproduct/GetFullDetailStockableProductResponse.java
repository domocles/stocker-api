package com.eep.stocker.dto.stockableproduct;

import com.eep.stocker.dto.stockableproduct.composite.IStockableProductCompositeDTO;
import com.eep.stocker.dto.stockableproduct.composite.StockableProductDeliveryCompositeDTO;
import com.eep.stocker.dto.stockableproduct.composite.StockableProductPurchaseOrderCompositeDTO;
import com.eep.stocker.dto.stockableproduct.composite.StockableProductSupplierQuoteCompositeDTO;
import com.eep.stocker.dto.stockableproductnote.GetStockableProductNoteLowDetailResponse;
import com.eep.stocker.dto.stocktransaction.GetStockTransactionLowDetailResponse;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;

/***
 * @author Sam Burns
 * @version 1.0
 * 05/10/2022
 *
 * Response DTO for the full detail stockable product response
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class GetFullDetailStockableProductResponse extends GetStockableProductResponse implements
        IStockableProductCompositeDTO.StockTransactions,
        IStockableProductCompositeDTO.PurchaseOrders,
        IStockableProductCompositeDTO.Deliveries,
        IStockableProductCompositeDTO.SupplierQuotes,
        IStockableProductCompositeDTO.Notes {
    @Singular
    private List<StockableProductPurchaseOrderCompositeDTO> purchaseOrders;

    @Singular
    private List<StockableProductDeliveryCompositeDTO> deliveries;

    @Singular
    private List<GetStockTransactionLowDetailResponse> stockTransactions;

    @Singular
    private List<StockableProductSupplierQuoteCompositeDTO> quotes;

    @Singular
    private List<GetStockableProductNoteLowDetailResponse> notes;
}
