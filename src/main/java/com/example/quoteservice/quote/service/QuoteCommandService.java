package com.example.quoteservice.quote.service;

import com.example.quoteservice.quote.common.exception.BusinessException;
import com.example.quoteservice.quote.common.exception.NotFoundException;
import com.example.quoteservice.quote.dto.QuoteCreateRequest;
import com.example.quoteservice.quote.dto.QuoteResponse;
import com.example.quoteservice.quote.mapper.QuoteMapper;
import com.example.quoteservice.quote.model.Quote;
import com.example.quoteservice.quote.model.QuoteStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class QuoteCommandService {

    private final Map<String, Quote> quoteStore = new ConcurrentHashMap<>();

    private final QuoteMapper quoteMapper;

    public QuoteCommandService(QuoteMapper quoteMapper) {
        this.quoteMapper = quoteMapper;
    }

    public QuoteResponse create(QuoteCreateRequest request) {
        LocalDateTime now = LocalDateTime.now();

        Quote quote = Quote.builder()
                .id(UUID.randomUUID().toString())
                .quoteNumber(generateQuoteNumber())
                .customerName(request.getCustomerName())
                .productCode(request.getProductCode())
                .premium(request.getPremium())
                .status(QuoteStatus.DRAFT)
                .createdAt(now)
                .updatedAt(now)
                .build();

        quoteStore.put(quote.getId(), quote);

        return quoteMapper.toResponse(quote);
    }

    public QuoteResponse submit(String id) {
        Quote quote = findQuote(id);

        if (quote.getStatus() != QuoteStatus.DRAFT) {
            throw new BusinessException("Only DRAFT quote can be submitted");
        }

        quote.setStatus(QuoteStatus.SUBMITTED);
        quote.setUpdatedAt(LocalDateTime.now());

        return quoteMapper.toResponse(quote);
    }

    public QuoteResponse approve(String id) {
        Quote quote = findQuote(id);

        if (quote.getStatus() != QuoteStatus.SUBMITTED) {
            throw new BusinessException("Only SUBMITTED quote can be approved");
        }

        quote.setStatus(QuoteStatus.APPROVED);
        quote.setUpdatedAt(LocalDateTime.now());

        return quoteMapper.toResponse(quote);
    }

    public Quote findQuote(String id) {
        Quote quote = quoteStore.get(id);

        if (quote == null) {
            throw new NotFoundException("Quote not found: " + id);
        }

        return quote;
    }

    public Map<String, Quote> getQuoteStore() {
        return quoteStore;
    }

    private String generateQuoteNumber() {
        return "Q-" + System.currentTimeMillis();
    }
}
