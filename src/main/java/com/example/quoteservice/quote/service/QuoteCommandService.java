package com.example.quoteservice.quote.service;

import com.example.quoteservice.quote.common.exception.BusinessException;
import com.example.quoteservice.quote.common.exception.NotFoundException;
import com.example.quoteservice.quote.dto.QuoteCreateRequest;
import com.example.quoteservice.quote.dto.QuoteResponse;
import com.example.quoteservice.quote.entity.QuoteEntity;
import com.example.quoteservice.quote.mapper.QuoteMapper;
import com.example.quoteservice.quote.model.QuoteStatus;
import com.example.quoteservice.quote.repository.QuoteRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class QuoteCommandService {

    private final QuoteRepository quoteRepository;
    private final QuoteMapper quoteMapper;

    public QuoteCommandService(QuoteRepository quoteRepository, QuoteMapper quoteMapper) {
        this.quoteRepository = quoteRepository;
        this.quoteMapper = quoteMapper;
    }

    @Transactional
    public QuoteResponse create(QuoteCreateRequest request) {
        LocalDateTime now = LocalDateTime.now();

        QuoteEntity quote = QuoteEntity.builder()
                .id(UUID.randomUUID().toString())
                .quoteNumber(generateQuoteNumber())
                .customerName(request.getCustomerName())
                .productCode(request.getProductCode())
                .premium(request.getPremium())
                .status(QuoteStatus.DRAFT)
                .createdAt(now)
                .updatedAt(now)
                .build();

        QuoteEntity saved = quoteRepository.save(quote);

        return quoteMapper.toResponse(saved);
    }

    @Transactional
    public QuoteResponse submit(String id) {
        QuoteEntity quote = findQuote(id);

        if (quote.getStatus() != QuoteStatus.DRAFT) {
            throw new BusinessException("Only DRAFT quote can be submitted");
        }

        quote.setStatus(QuoteStatus.SUBMITTED);
        quote.setUpdatedAt(LocalDateTime.now());

        return quoteMapper.toResponse(quote);
    }

    @Transactional
    public QuoteResponse approve(String id) {
        QuoteEntity quote = findQuote(id);

        if (quote.getStatus() != QuoteStatus.SUBMITTED) {
            throw new BusinessException("Only SUBMITTED quote can be approved");
        }

        quote.setStatus(QuoteStatus.APPROVED);
        quote.setUpdatedAt(LocalDateTime.now());

        return quoteMapper.toResponse(quote);
    }

    @Transactional(readOnly = true)
    public QuoteEntity findQuote(String id) {
        return quoteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Quote not found: " + id));
    }

    private String generateQuoteNumber() {
        return "Q-" + System.currentTimeMillis();
    }
}
