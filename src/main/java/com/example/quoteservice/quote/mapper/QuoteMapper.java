package com.example.quoteservice.quote.mapper;

import com.example.quoteservice.quote.dto.QuoteDetailResponse;
import com.example.quoteservice.quote.dto.QuoteListItemResponse;
import com.example.quoteservice.quote.dto.QuoteResponse;
import com.example.quoteservice.quote.entity.QuoteEntity;
//import com.example.quoteservice.quote.model.Quote;
import com.example.quoteservice.quote.model.QuoteStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QuoteMapper {
    public QuoteResponse toResponse (QuoteEntity quote) {
        return new QuoteResponse(
                quote.getId(),
                quote.getQuoteNumber(),
                quote.getStatus().name()
        );
    }

    public QuoteListItemResponse toListItemResponse (QuoteEntity quote) {
        return new QuoteListItemResponse(
                quote.getId(),
                quote.getQuoteNumber(),
                quote.getCustomerName(),
                quote.getProductCode(),
                quote.getPremium(),
                quote.getStatus().name()
        );
    }

    public QuoteDetailResponse toDetailResponse(QuoteEntity quote) {
        return new QuoteDetailResponse(
                quote.getId(),
                quote.getQuoteNumber(),
                quote.getCustomerName(),
                quote.getProductCode(),
                quote.getPremium(),
                quote.getStatus().name(),
                quote.getCreatedAt(),
                quote.getUpdatedAt(),
                buildAvailableActions(quote)
        );
    }

    private List<String> buildAvailableActions (QuoteEntity quote){
        List<String> actions = new ArrayList<>();
        if (quote.getStatus() == QuoteStatus.DRAFT) {
            actions.add("SUBMIT");
        }

        if (quote.getStatus() == QuoteStatus.SUBMITTED) {
            actions.add("APPROVE");
        }

        return actions;
    }
}