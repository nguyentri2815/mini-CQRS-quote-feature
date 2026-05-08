package com.example.quoteservice.quote.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class QuoteCreateRequest {
    @NotBlank(message = "customerName is required")
    private String customerName;

    @NotBlank(message = "productCode is required")
    private String productCode;

    @NotNull(message = "premium is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "premium must be greater than 0")
    private BigDecimal premium;

}