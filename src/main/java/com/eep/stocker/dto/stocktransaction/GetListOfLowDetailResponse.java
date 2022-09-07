package com.eep.stocker.dto.stocktransaction;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/***
 * @author Sam Burns
 * @version 1.0
 * 06/09/2022
 */
@Data
public class GetListOfLowDetailResponse {
    public final List<GetStockTransactionLowDetailResponse> allStockTransactions = new ArrayList<>();

    public void addStockTransaction(GetStockTransactionLowDetailResponse response) {
        this.allStockTransactions.add(response);
    }
}
