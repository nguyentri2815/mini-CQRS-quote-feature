package com.example.quoteservice.quote.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuoteResponse {
    private String id;

    private String QuoteNumber;

    private String status;
}

