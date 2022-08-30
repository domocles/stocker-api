package com.eep.stocker.controllers.error;

import com.eep.stocker.controllers.error.exceptions.*;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;

/***
 * @author Sam Burns
 * @version 1.0
 * 24/08/2022
 * Controller advice to handle exceptions
 */
@ControllerAdvice
public class StockerExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String INCORRECT_REQUEST = "INCORRECT_REQUEST";
    private static final String BAD_REQUEST = "BAD_REQUEST";
    private static final String CONFLICT_MPN = "CONFLICTING MPN";
    private static final String NOT_FOUND = "NOT FOUND";

    /***
     * Handles the case where an Assembly Line is requested which does not exist
     * @param ex - AssemblyLineDoesNotExistException
     * @return a response entity with the response status set to Not_Found (404)
     */
    @ExceptionHandler(AssemblyLineDoesNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ResponseEntity<ErrorResponse> handleAssemblyLineDoesNotExist(AssemblyLineDoesNotExistException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse(NOT_FOUND, details);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /***
     * Handles the case where an Assembly is requested which does not exist
     * @param ex - AssemblyDoesNotExistException
     * @return a response entity with the response status set to Not_Found (404)
     */
    @ExceptionHandler(AssemblyDoesNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ResponseEntity<ErrorResponse> handleAssemblyDoesNotExist(AssemblyDoesNotExistException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse(NOT_FOUND, details);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /***
     * Handles the case where a Delivery Line is requested which does not exist
     * @param ex - DeliveryLineDoesNotExistException
     * @return a response entity with the response status set to Not_Found (404)
     */
    @ExceptionHandler(DeliveryLineDoesNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ResponseEntity<ErrorResponse> handleDeliveryLineNotFound(DeliveryLineDoesNotExistException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse(BAD_REQUEST, details);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /***
     * Handles the case where a Delivery is requested which does not exist
     * @param ex - DeliveryDoesNotExistException
     * @return a response entity with the response status set to Not_Found (404)
     */
    @ExceptionHandler(DeliveryDoesNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ResponseEntity<ErrorResponse> handleDeliveryNotFound(DeliveryDoesNotExistException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse(BAD_REQUEST, details);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /***
     * Handles the case where a Purchase Order is requested which does not exist
     * @param ex - PurchaseOrderDoesNotExistException
     * @return a response entity with the response status set to Not_Found (404)
     */
    @ExceptionHandler(PurchaseOrderDoesNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ResponseEntity<ErrorResponse> handlePurchaseOrderNotFound(PurchaseOrderDoesNotExistException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse(BAD_REQUEST, details);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /***
     * Handles the case where a StockableProduct is requested which does not exist
     * @param ex - StockableProductDoesNotExistException
     * @return a response entity with the response status set to Not_Found (404)
     */
    @ExceptionHandler(StockableProductDoesNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ResponseEntity<ErrorResponse> handleStockableProductNotFound(StockableProductDoesNotExistException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse(BAD_REQUEST, details);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /***
     * Handles the case where a Supplier is requested which does not exist
     * @param ex - SupplierDoesNotExistException
     * @return a response entity with the response status set to Not_Found (404)
     */
    @ExceptionHandler(SupplierDoesNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ResponseEntity<ErrorResponse> handleSupplierNotFound(SupplierDoesNotExistException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse(BAD_REQUEST, details);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /***
     * Handles the case where a record is requested which does not exist
     * @param ex - AssemblyDoesNotExistException
     * @return a response entity with the response status set to Not_Found (404)
     */
    @ExceptionHandler(RecordNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleRecordNotFound(RecordNotFoundException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse(INCORRECT_REQUEST, details);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /***
     * Handles the case where an MPN is not unique
     * @param ex - MpnNotUniqueNotExistException
     * @return a response entity with the response status set to Not_Found (404)
     */
    @ExceptionHandler(MpnNotUniqueException.class)
    public final ResponseEntity<ErrorResponse> handleMpnNotUnique(MpnNotUniqueException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse(CONFLICT_MPN, details);
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /***
     * Handles the case where an MPN is requested which does not exist
     * @param ex - MpnNotFoundException
     * @return a response entity with the response status set to Not_Found (404)
     */
    @ExceptionHandler(MpnNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleMpnNotFound(MpnNotFoundException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse(NOT_FOUND, details);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /***
     * Handles the case where an PurchaseOrderLine is requested which does not exist
     * @param ex - PurchaseOrderLineDoesNotExistException
     * @return a response entity with the response status set to Not_Found (404)
     */
    @ExceptionHandler(PurchaseOrderLineDoesNotExistException.class)
    public final ResponseEntity<ErrorResponse> handlePurchaseOrderLineDoesNotExistException(PurchaseOrderLineDoesNotExistException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse(NOT_FOUND, details);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /***
     * Handles the case where a Stock Transaction is requested which does not exist
     * @param ex - StockTransactionDoesNotExistException
     * @return a response entity with the response status set to Not_Found (404)
     */
    @ExceptionHandler(StockTransactionDoesNotExistException.class)
    public final ResponseEntity<ErrorResponse> handleStockTransactionDoesNotExistException(StockTransactionDoesNotExistException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse(NOT_FOUND, details);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /***
     * Handles the case where a ReferenceGenerator is requested which does not exist
     * @param ex - ReferenceGeneratorDoesNotExistException
     * @return a response entity with the response status set to Not_Found (404)
     */
    @ExceptionHandler(ReferenceGeneratorDoesNotExistException.class)
    public final ResponseEntity<ErrorResponse> handleReferenceGeneratorDoesNotExistException(ReferenceGeneratorDoesNotExistException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse(NOT_FOUND, details);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /***
     * Handles the case where a ReferenceGenerator already exists
     * @param ex - ReferenceGeneratorAlreadyExistsException
     * @return a response entity with the response status set to Not_Found (404)
     */
    @ExceptionHandler(ReferenceGeneratorAlreadyExistsException.class)
    public final ResponseEntity<ErrorResponse> handleReferenceGeneratorAlreadyExistsException(ReferenceGeneratorAlreadyExistsException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse(BAD_REQUEST, details);
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /***
     * Handles the case where a DomainObject already exists
     * @param ex - DomainObjectAlreadyExistsException
     * @return a response entity with the response status set to Not_Found (404)
     */
    @ExceptionHandler(DomainObjectAlreadyExistsException.class)
    public final ResponseEntity<ErrorResponse> handleDomainObjectAlreadyExistsException(DomainObjectAlreadyExistsException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse(BAD_REQUEST, details);
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /***
     * Handles the case where a DomainObject is requested which does not exist
     * @param ex - DomainObjectDoesNotExistException
     * @return a response entity with the response status set to Not_Found (404)
     */
    @ExceptionHandler(DomainObjectDoesNotExistException.class)
    public final ResponseEntity<ErrorResponse> handleDomainObjectDoesNotExistException(DomainObjectDoesNotExistException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse(BAD_REQUEST, details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /***
     * Handles the case where a constrain is violated
     * @param ex - ConstraintViolationException
     * @return a response entity with the response status set to Not_Found (404)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse(BAD_REQUEST, details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /***
     * Handles the case where there is a validation problem
     * @param ex - ValidationException
     * @return a response entity with the response status set to Not_Found (404)
     */
    @ExceptionHandler(ValidationException.class)
    public final ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse(BAD_REQUEST, details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        return new ResponseEntity<>(new ErrorResponse(BAD_REQUEST, errors), HttpStatus.BAD_REQUEST);
    }
}
