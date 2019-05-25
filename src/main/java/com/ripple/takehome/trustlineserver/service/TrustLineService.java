package com.ripple.takehome.trustlineserver.service;

import com.ripple.takehome.trustlineserver.payload.TransactionRequest;

public interface TrustLineService {

    void sendTransaction(TransactionRequest transactionRequest);

    void receiveTransaction(TransactionRequest transactionRequest);

}
