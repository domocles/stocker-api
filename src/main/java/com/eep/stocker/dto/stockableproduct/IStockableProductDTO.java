package com.eep.stocker.dto.stockableproduct;

import java.math.BigDecimal;
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
}
