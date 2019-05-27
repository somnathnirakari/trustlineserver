package com.ripple.takehome.trustlineserver.payload;

import lombok.Data;

/**
 * Response class for all APIs in the controller layer
 * @author Somnath Nirakari
 */
@Data
public class ApiResponse {
    private Boolean success;
    private String message;

    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
