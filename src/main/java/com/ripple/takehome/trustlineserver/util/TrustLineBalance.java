package com.ripple.takehome.trustlineserver.util;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class TrustLineBalance {
    private double amount = 0;

    public double update(double amount) {
        synchronized (this) {
            this.amount+=amount;
        }

        return amount;
    }
}
