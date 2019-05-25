package com.ripple.takehome.trustlineserver.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TransactionRequest {
    String from;
    @NotNull
    String to;
    @NotNull
    double amount;
}
