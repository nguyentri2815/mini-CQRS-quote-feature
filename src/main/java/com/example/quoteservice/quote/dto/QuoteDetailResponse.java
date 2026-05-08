package com.example.quoteservice.quote.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public  class QuoteDetailResponse {
    private String id;

    private String quoteNumber;

    private String customerName;

    private String productCode;

    private BigDecimal premium;

    private String status;

    private LocalDateTime createAt;

    private LocalDateTime updatedAt;

    private List<String> availableActions;
}