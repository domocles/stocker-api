package com.eep.stocker.dto.stockableproduct.composite;

import com.eep.stocker.dto.stockableproductnote.GetStockableProductNoteLowDetailResponse;
import com.eep.stocker.dto.stocktransaction.GetStockTransactionLowDetailResponse;

import java.util.List;

/***
 * Composite DTO interfaces for the {@link com.eep.stocker.domain.StockableProduct}
 */
public interface IStockableProductCompositeDTO {
    interface Delivery { StockableProductDeliveryCompositeDTO getDelivery(); }
    interface Deliveries {
        List<StockableProductDeliveryCompositeDTO> getDeliveries();
        default void addDelivery(StockableProductDeliveryCompositeDTO delivery) {
            getDeliveries().add(delivery);
        }
    }
    interface DeliveryLine { StockableProductDeliveryLineCompositeDTO getDeliveryLine(); }
    interface DeliveryLines {
        List<StockableProductDeliveryLineCompositeDTO> getDeliveryLines();
        default void addDeliveryLine(StockableProductDeliveryLineCompositeDTO deliveryLine) {
            getDeliveryLines().add(deliveryLine);
        }
    }

    interface PurchaseOrder { StockableProductPurchaseOrderCompositeDTO getPurchaseOrder(); }
    interface PurchaseOrders {
        List<StockableProductPurchaseOrderCompositeDTO> getPurchaseOrders();
        default void addOrder(StockableProductPurchaseOrderCompositeDTO order) {
            getPurchaseOrders().add(order);
        }
    }

    interface PurchaseOrderLine { StockableProductPurchaseOrderLineCompositeDTO getPurchaseOrderLine(); }
    interface PurchaseOrderLines {
        List<StockableProductPurchaseOrderLineCompositeDTO> getOrderLines();
        default void addOrderLine(StockableProductPurchaseOrderLineCompositeDTO orderLine) {
            getOrderLines().add(orderLine);
        }
    }

    interface StockTransactions {
        List<GetStockTransactionLowDetailResponse> getStockTransactions();
        default void addTransaction(GetStockTransactionLowDetailResponse transaction) {
            getStockTransactions().add(transaction);
        }
    }

    interface SupplierQuotes {
        List<StockableProductSupplierQuoteCompositeDTO> getQuotes();
        default void addQuote(StockableProductSupplierQuoteCompositeDTO quote) {
            getQuotes().add(quote);
        }
    }

    interface Notes {
        List<GetStockableProductNoteLowDetailResponse> getNotes();
        default void addNote(GetStockableProductNoteLowDetailResponse note) {
            getNotes().add(note);
        }
    }

}
