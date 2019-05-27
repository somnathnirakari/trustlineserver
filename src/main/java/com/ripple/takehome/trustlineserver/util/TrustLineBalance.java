package com.ripple.takehome.trustlineserver.util;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * represents trustline balance
 * @author Somnath Nirakari
 */
@Component
@Data
public class TrustLineBalance {
    private double amount = 0;

    /**
     * threadsafe method to update thetrustline balance
     * @param amount
     * @return new balance
     */
    public double update(double amount) {
        synchronized (this) {
            this.amount+=amount;
        }

        return this.amount;
    }
}
