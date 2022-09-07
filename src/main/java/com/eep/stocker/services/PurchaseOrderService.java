package com.eep.stocker.services;

import com.eep.stocker.domain.PurchaseOrder;
import com.eep.stocker.domain.Supplier;
import com.eep.stocker.repository.IPurchaseOrderRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class PurchaseOrderService {
    /***
     * Repository for retrieving Purchase Orders from the data source
     */
    private IPurchaseOrderRepository purchaseOrderRepository;

    /***
     * Gets all purchase orders in the database
     * @return - a List of all purchase orders
     */
    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    /***
     * Returns a list of all purchase orders by supplier
     * @param supplier - the supplier whose purchase orders we wish to get
     * @return - a list of purchase orders, if none exist will return List.empty();
     */
    public List<PurchaseOrder> getAllPurchaseOrdersForSupplier(Supplier supplier) {
        //todo: test that returns empty list of no purchase orders exist
        return purchaseOrderRepository.findAllBySupplier(supplier);
    }

    /***
     * Gets all purchase orders between dates
     * @param start - the start date
     * @param end - the end date
     * @return - a list of purchase orders
     */
    public List<PurchaseOrder> getAllPurchaseOrdersBetween(LocalDate start, LocalDate end) {
        return purchaseOrderRepository.findAllByPurchaseOrderDateBetween(start, end);
    }

    /***
     * Get all purchase orders by their reference
     * @param ref the reference to search for
     * @return a list containing all purchase orders with the supplied reference
     */
    public Optional<PurchaseOrder> getPurchaseOrderByReference(String ref) {
        return purchaseOrderRepository.findByPurchaseOrderReference(ref);
    }

    /***
     * Get all purchase orders by their reference
     * @param reference the reference to search for
     * @return a list containing all purchase orders with the supplied reference
     */
    public List<PurchaseOrder> getAllPurchaseOrdersBySupplierReference(String reference) {
        return purchaseOrderRepository.findAllBySupplierOrderReference(reference);
    }

    /***
     * Saves a purchase order to the database
     * @param purchaseOrder - the purchase order to persist
     * @return - the persisted purchase order
     */
    public PurchaseOrder savePurchaseOrder(PurchaseOrder purchaseOrder) {
        return purchaseOrderRepository.save(purchaseOrder);
    }

    /***
     * Find a purchase order by it's id.
     * @param purchaseOrderId - the id to search the database for
     * @return - an Optioanl containing the purchase order if it exists else Optional.empty()
     */
    public Optional<PurchaseOrder> getPurchaseOrderFromId(Long purchaseOrderId) {
        return purchaseOrderRepository.findById(purchaseOrderId);
    }

    /***
     * Find a purchase order by it's uid
     * @param uid - the uid of the purchase order
     * @return - an Optional containing the purchase ordered if it exists else Optional.empty()
     */
    public Optional<PurchaseOrder> getPurchaseOrderFromUid(UUID uid) {
        return purchaseOrderRepository.findByUid(uid);
    }

    /***
     * Deletes a purchase order by it's unique id
     * @param purchaseOrder the unique id of the purchase order
     */
    public void deletePurchaseOrder(PurchaseOrder purchaseOrder) {
        //todo return deleted purchase order or Optional.empty
        purchaseOrderRepository.delete(purchaseOrder);
    }
}
