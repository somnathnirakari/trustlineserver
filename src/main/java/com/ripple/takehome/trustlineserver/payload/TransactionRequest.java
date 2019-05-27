package com.ripple.takehome.trustlineserver.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Class that represents transaction request between client and a node or between 2 trustline nodes
 * @author Somnath Nirakari
 */
@Data
public class TransactionRequest {
    String from;
    @NotNull
    String to;
    @NotNull
    double amount;
}
