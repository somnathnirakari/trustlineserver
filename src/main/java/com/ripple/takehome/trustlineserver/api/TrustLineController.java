package com.ripple.takehome.trustlineserver.api;

import com.ripple.takehome.trustlineserver.payload.ApiResponse;
import com.ripple.takehome.trustlineserver.payload.TransactionRequest;
import com.ripple.takehome.trustlineserver.service.TrustLineService;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

/**
 * @author Somnath Nirakari
 */
@RestController
@RequestMapping("/api")
public class TrustLineController {

    private String mdcTransactionId = "MDC_TRANSACTION_ID";

    @Autowired
    TrustLineService trustLineService;

    /**
     * this endpoint is invoked by the client
     * @param transactionRequest
     * @return apiResponse
     */
    @PostMapping("/transactions")
    ResponseEntity<?> processTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {

        // Ideally this should be in a pre request filter
        // Although we won't use the transactionId to tie up the transaction,
        // we will use it in the API response header.
        // In a real system, this id would tie the request and response and everything in between.
        String transactionId = UUID.randomUUID().toString().toUpperCase().replace("-", "");
        MDC.put(mdcTransactionId, transactionId);

        trustLineService.sendTransaction(transactionRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{transactionId}")
                .buildAndExpand(transactionId).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Success!!"));
    }

    /**
     * this endpoint is invoked by the sender node
     * @param transactionRequest
     * @return apiResponse
     */
    @PostMapping("/trustline/transactions")
    ResponseEntity<?> receiveTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {

        // Ideally this should be in a pre request filter
        // See in the processTransaction method
        String mdcTransactionId = UUID.randomUUID().toString().toUpperCase().replace("-", "");
        MDC.put(mdcTransactionId, mdcTransactionId);

        trustLineService.receiveTransaction(transactionRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{transactionId}")
                .buildAndExpand(mdcTransactionId).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Success!!"));    }
}
