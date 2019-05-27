package com.ripple.takehome.trustlineserver.service;

import com.ripple.takehome.trustlineserver.payload.TransactionRequest;

/**
 * Interface to trustline service
 * @author Somnath Nirakari
 */
public interface TrustLineService {

    /**
     * This method sends a transaction to the receving node based on transaction request.
     * @param transactionRequest
     */
    void sendTransaction(TransactionRequest transactionRequest);

    /**
     * This method receives a transaction from a sending node based on transaction request.
     * @param transactionRequest
     */
    void receiveTransaction(TransactionRequest transactionRequest);

}
