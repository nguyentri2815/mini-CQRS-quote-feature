package com.example.quoteservice.quote.mapper;

import com.example.quoteservice.quote.document.QuoteDocument;
import com.example.quoteservice.quote.entity.QuoteEntity;
import org.springframework.stereotype.Component;

@Component
public class QuoteSearchMapper {

    public QuoteDocument toDocument(QuoteEntity entity) {
        return QuoteDocument.builder()
                .id(entity.getId())
                .quoteNumber(entity.getQuoteNumber())
                .customerName(entity.getCustomerName())
                .productCode(entity.getProductCode())
                .premium(entity.getPremium())
                .status(entity.getStatus().name())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
