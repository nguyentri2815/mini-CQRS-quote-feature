package com.example.quoteservice.quote.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class QuoteListItemResponse {

    private String id;

    private String quoteNumber;

    private String customerName;

    private String productCode;

    private BigDecimal premium;

    private String status; // tại sao là String mà ko phải enum?
}