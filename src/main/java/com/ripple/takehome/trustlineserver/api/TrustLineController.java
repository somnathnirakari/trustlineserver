package com.ripple.takehome.trustlineserver.api;

import com.ripple.takehome.trustlineserver.payload.ApiResponse;
import com.ripple.takehome.trustlineserver.payload.TransactionRequest;
import com.ripple.takehome.trustlineserver.service.TrustLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class TrustLineController {

    @Autowired
    TrustLineService trustLineService;

    @PostMapping("/transactions")
    ResponseEntity<?> processTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {

        trustLineService.sendTransaction(transactionRequest);
        return ResponseEntity.ok().body(new ApiResponse(true,"Success!!"));
    }

    @PostMapping("/trustline/transactions")
    ResponseEntity<?> sendTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {

        trustLineService.receiveTransaction(transactionRequest);
        return ResponseEntity.ok().body(new ApiResponse(true,"Success!!"));
    }
}
