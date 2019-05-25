package com.ripple.takehome.trustlineserver.api;

import com.ripple.takehome.trustlineserver.payload.TransactionRequest;
import com.ripple.takehome.trustlineserver.service.TrustLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    ResponseEntity<Object> processTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
       ResponseEntity<Object> responseEntity = ResponseEntity.ok().build();
        try {
            trustLineService.sendTransaction(transactionRequest);
        } catch (Exception ex) {
            responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        return responseEntity;
    }

    @PostMapping("/trustline/transactions")
    ResponseEntity<Object> sendTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
        ResponseEntity<Object> responseEntity = ResponseEntity.ok().build();
        try{
            trustLineService.receiveTransaction(transactionRequest);
        } catch (Exception ex) {
            responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        return responseEntity;
    }
}
