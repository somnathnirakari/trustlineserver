package com.ripple.takehome.trustlineserver.util;

import com.ripple.takehome.trustlineserver.payload.TransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Utility class which acts as a wrapper for handling REST methods
 * @author Somnath Nirakari
 */
@Component
public class RestUtil {

    RestTemplate restTemplate;

    @Autowired
    public RestUtil(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    /**
     * post a transaction to receiver node
     * @param url
     * @param transactionRequest
     */
    public void postTransaction(String url, TransactionRequest transactionRequest) {
        HttpEntity<TransactionRequest> request = new HttpEntity<>(transactionRequest);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (response.getStatusCode()!= HttpStatus.CREATED && response.getStatusCode()!= HttpStatus.OK) {
            throw new RuntimeException("Invalid Response from " + transactionRequest.getTo());
        }
    }
}
