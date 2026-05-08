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
    private final QuoteIndexService quoteIndexService;

    public QuoteCommandService(QuoteRepository quoteRepository, QuoteMapper quoteMapper, QuoteIndexService quoteIndexService) {
        this.quoteRepository = quoteRepository;
        this.quoteMapper = quoteMapper;
        this.quoteIndexService = quoteIndexService;
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
        quoteIndexService.syncQuote(saved.getId());

        return quoteMapper.toResponse(saved);
    }

    @Transactional
    public QuoteResponse submit(String id) {
        QuoteEntity quote = findQuoteOrThrow(id);

        validateCanSubmit(quote);

        quote.setStatus(QuoteStatus.SUBMITTED);
        quote.setUpdatedAt(LocalDateTime.now());

        QuoteEntity savedQuote = quoteRepository.save(quote);
        quoteIndexService.syncQuote(savedQuote.getId());

        return quoteMapper.toResponse(savedQuote);
    }

    @Transactional
    public QuoteResponse approve(String id) {
        QuoteEntity quote = findQuoteOrThrow(id);

        validateCanApprove(quote);

        quote.setStatus(QuoteStatus.APPROVED);
        quote.setUpdatedAt(LocalDateTime.now());

        QuoteEntity savedQuote = quoteRepository.save(quote);
        quoteIndexService.syncQuote(savedQuote.getId());

        return quoteMapper.toResponse(savedQuote);
    }

    @Transactional
    public QuoteResponse reject(String id){
        QuoteEntity quote = findQuoteOrThrow(id);

        validaCanReject(quote);

        quote.setStatus(QuoteStatus.REJECTED);
        quote.setUpdatedAt(LocalDateTime.now());

//        QuoteEntity savedQuote = quoteRepository.save(quote);
        return quoteMapper.toResponse(quote);
    }

    @Transactional(readOnly = true)
    public QuoteEntity findQuoteOrThrow(String id) {
        return quoteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Quote not found: " + id));
    }

    public void validateCanSubmit (QuoteEntity quote){
        if (quote.getStatus() != QuoteStatus.DRAFT) {
            throw new BusinessException("Only DRAFT quote can be submitted");
        }
    }

    public void validateCanApprove (QuoteEntity quote){
        if (quote.getStatus() != QuoteStatus.DRAFT) {
            throw new BusinessException("Only DRAFT quote can be submitted");
        }
    }

    public void validaCanReject(QuoteEntity quote){
        if (quote.getStatus() != QuoteStatus.SUBMITTED){
            throw  new BusinessException("Only SUBMITTED qupte can be rejected. Current status:" + quote.getStatus());
        }
    }

    private String generateQuoteNumber() {
        return "Q-" + System.currentTimeMillis();
    }
}
