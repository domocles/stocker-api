package com.eep.stocker.dto.supplierquote;

import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.domain.SupplierQuote;
import com.eep.stocker.dto.MapperUtils;
import com.eep.stocker.dto.stockableproduct.StockableProductMapper;
import com.eep.stocker.dto.supplier.SupplierMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/***
 * @author Sam Burns
 * @version 1.0
 * 09/09/2022
 *
 * Mapper for mapping Supplier Quotes to request and response objects
 */
@Mapper(componentModel = "spring", uses = {MapperUtils.class, StockableProductMapper.class, SupplierMapper.class})
public interface SupplierQuoteMapper {
    @Mapping(target = "id", source = "uid")
    GetSupplierQuoteResponse mapToGetResponse(SupplierQuote quote);

    @Mapping(target = "id", source = "uid")
    CreateSupplierQuoteResponse mapToCreateResponse(SupplierQuote quote);

    @Mapping(target = "id", source = "uid")
    DeleteSupplierQuoteResponse mapToDeleteResponse(SupplierQuote quote);

    @Mapping(target = "id", source = "uid")
    UpdateSupplierQuoteResponse mapToUpdateResponse(SupplierQuote quote);

    @Mapping(target = "id", source = "uid")
    GetLowDetailSupplierQuoteResponse mapToLowDetailResponse(SupplierQuote quote);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uid", ignore = true)
    SupplierQuote mapFromCreateRequest(CreateSupplierQuoteRequest request, Supplier supplier, StockableProduct stockableProduct);

    @Mapping(target = "id", ignore = true)
    void updateFromUpdateRequest(@MappingTarget SupplierQuote quote, UpdateSupplierQuoteRequest request);
}
