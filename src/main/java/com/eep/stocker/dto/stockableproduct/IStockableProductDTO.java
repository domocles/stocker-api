package com.eep.stocker.dto.stockableproduct;

import com.eep.stocker.dto.delivery.GetDeliveryResponse;
import com.eep.stocker.dto.purchaseorder.GetPurchaseOrderHighDetailResponse;
import com.eep.stocker.dto.stockableproduct.composite.StockableProductPurchaseOrderCompositeDTO;
import com.eep.stocker.dto.stocktransaction.GetStockTransactionResponse;
import com.eep.stocker.dto.supplierquote.GetSupplierQuoteResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface IStockableProductDTO {
    interface Name{ String getName(); }
    interface Mpn{ String getMpn(); }
    interface Id{ String getId(); }
    interface Description{ String getDescription(); }
    interface Category{ String getCategory(); }
    interface Units{ String getUnits(); }
    interface Tags{ Set<String> getTags(); }
    interface TagString{ String getTagString(); }
    interface StockPrice{ BigDecimal getStockPrice(); }
    interface InStock{ BigDecimal getInStock(); }
    interface OnOrder{ BigDecimal getOnOrder(); }
    interface ProjectedQty{ BigDecimal getProjectedQty(); }
    interface Orders{ List<StockableProductPurchaseOrderCompositeDTO> getOrders(); }
    interface Deliveries{ List<GetDeliveryResponse> getDeliveries(); }
    interface StockTransactions{ List<GetStockTransactionResponse> getStockTransactions(); }
    interface SupplierQuotes{ List<GetSupplierQuoteResponse> getSupplierQuotes(); }

}
