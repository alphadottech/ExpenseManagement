package com.adt.expensemanagement.utilities.errorResponseUtilities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class FieldErrors {
    private int status;
    private String message;
    private Map<String, String> field_errors;

    public FieldErrors(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
