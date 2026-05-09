package com.example.quoteservice.quote.service;

import com.example.quoteservice.common.exception.NotFoundException;
import com.example.quoteservice.quote.dto.QuoteDetailResponse;
import com.example.quoteservice.quote.dto.QuoteListItemResponse;
import com.example.quoteservice.quote.entity.QuoteEntity;
import com.example.quoteservice.quote.mapper.QuoteMapper;
import com.example.quoteservice.quote.model.QuoteStatus;
import com.example.quoteservice.quote.repository.QuoteRepository;
import com.example.quoteservice.workflow.service.WorkflowService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class QuoteQueryService {

    private final QuoteRepository quoteRepository;
    private final QuoteMapper quoteMapper;
    private final WorkflowService workflowService;

    public QuoteQueryService(
            QuoteRepository quoteRepository,
            QuoteMapper quoteMapper,
            WorkflowService workflowService
    ) {
        this.quoteRepository = quoteRepository;
        this.quoteMapper = quoteMapper;
        this.workflowService = workflowService;
    }

    @Transactional(readOnly = true)
    public QuoteDetailResponse detail(String id) {
        QuoteEntity quote = quoteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Quote not found: " + id));

        return toDetailResponse(quote);
    }

    @Transactional(readOnly = true)
    public List<QuoteListItemResponse> list(String keyword, String status) {
        return quoteRepository.findAll()
                .stream()
                .filter(quote -> matchKeyword(quote, keyword))
                .filter(quote -> matchStatus(quote, status))
                .sorted(Comparator.comparing(QuoteEntity::getCreatedAt).reversed())
                .map(quoteMapper::toListItemResponse)
                .toList();
    }

    private boolean matchKeyword(QuoteEntity quote, String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return true;
        }

        String lowerKeyword = keyword.toLowerCase();

        return quote.getQuoteNumber().toLowerCase().contains(lowerKeyword)
                || quote.getCustomerName().toLowerCase().contains(lowerKeyword)
                || quote.getProductCode().toLowerCase().contains(lowerKeyword);
    }

    private boolean matchStatus(QuoteEntity quote, String status) {
        if (!StringUtils.hasText(status)) {
            return true;
        }

        try {
            QuoteStatus expectedStatus = QuoteStatus.valueOf(status.toUpperCase());
            return quote.getStatus() == expectedStatus;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    private QuoteDetailResponse toDetailResponse(QuoteEntity quote) {
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

    private List<String> buildAvailableActions(QuoteEntity quote) {
        List<String> actions = new ArrayList<>();

        if (quote.getStatus() == QuoteStatus.DRAFT) {
            actions.add("SUBMIT");
        }

        if (quote.getStatus() == QuoteStatus.SUBMITTED
                && workflowService.hasOpenApproveQuoteTask(quote.getId())) {
            actions.add("APPROVE");
        }

        return actions;
    }

}
