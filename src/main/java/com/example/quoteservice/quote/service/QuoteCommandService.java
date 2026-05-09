package com.example.quoteservice.quote.service;

import com.example.quoteservice.common.exception.BusinessException;
import com.example.quoteservice.common.exception.NotFoundException;
import com.example.quoteservice.quote.dto.QuoteCreateRequest;
import com.example.quoteservice.quote.dto.QuoteResponse;
import com.example.quoteservice.quote.entity.QuoteEntity;
import com.example.quoteservice.quote.mapper.QuoteMapper;
import com.example.quoteservice.quote.messaging.QuoteSyncPublisher;
import com.example.quoteservice.quote.model.QuoteStatus;
import com.example.quoteservice.quote.repository.QuoteRepository;
import com.example.quoteservice.workflow.service.WorkflowService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class QuoteCommandService {

    private final QuoteRepository quoteRepository;
    private final QuoteMapper quoteMapper;
    private final QuoteSyncPublisher quoteSyncPublisher;
    private final WorkflowService workflowService;

    public QuoteCommandService(
            QuoteRepository quoteRepository,
            QuoteMapper quoteMapper,
            QuoteSyncPublisher quoteSyncPublisher,
            WorkflowService workflowService
    ) {
        this.quoteRepository = quoteRepository;
        this.quoteMapper = quoteMapper;
        this.quoteSyncPublisher = quoteSyncPublisher;
        this.workflowService = workflowService;
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

        QuoteEntity savedQuote = quoteRepository.save(quote);

        workflowService.createApproveQuoteTask(savedQuote.getId());

        quoteSyncPublisher.publishSyncEs(savedQuote.getId(), "CREATED");

        return quoteMapper.toResponse(savedQuote);
    }

    @Transactional
    public QuoteResponse submit(String id) {
        QuoteEntity quote = findQuoteOrThrow(id);

        validateCanSubmit(quote);

        workflowService.completeApproveQuoteTask(
                quote.getId(),
                "current-user-demo"
        );

        quote.setStatus(QuoteStatus.SUBMITTED);
        quote.setUpdatedAt(LocalDateTime.now());

        QuoteEntity savedQuote = quoteRepository.save(quote);
        quoteSyncPublisher.publishSyncEs(savedQuote.getId(), "SUBMITTED");

        return quoteMapper.toResponse(savedQuote);
    }

    @Transactional
    public QuoteResponse approve(String id) {
        QuoteEntity quote = findQuoteOrThrow(id);

        validateCanApprove(quote);

        quote.setStatus(QuoteStatus.APPROVED);
        quote.setUpdatedAt(LocalDateTime.now());

        QuoteEntity savedQuote = quoteRepository.save(quote);
        quoteSyncPublisher.publishSyncEs(savedQuote.getId(), "APPROVED");

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
        if (quote.getStatus() != QuoteStatus.SUBMITTED) {
            throw new BusinessException("Only SUBMITTED quote can be approved");
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
