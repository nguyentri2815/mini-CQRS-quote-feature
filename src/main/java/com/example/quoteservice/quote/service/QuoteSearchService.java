package com.example.quoteservice.quote.service;

import com.example.quoteservice.quote.document.QuoteDocument;
import com.example.quoteservice.quote.dto.QuoteListItemResponse;
import com.example.quoteservice.quote.search.QuoteSearchRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class QuoteSearchService {

    private final QuoteSearchRepository quoteSearchRepository;

    public QuoteSearchService(QuoteSearchRepository quoteSearchRepository) {
        this.quoteSearchRepository = quoteSearchRepository;
    }

    public List<QuoteListItemResponse> list(String keyword, String status) {
        return StreamSupport.stream(quoteSearchRepository.findAll().spliterator(), false)
                .filter(document -> matchKeyword(document, keyword))
                .filter(document -> matchStatus(document, status))
                .sorted(Comparator.comparing(QuoteDocument::getCreatedAt).reversed())
                .map(this::toListItemResponse)
                .toList();
    }

    private boolean matchKeyword(QuoteDocument document, String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return true;
        }

        String lowerKeyword = keyword.toLowerCase();

        return document.getQuoteNumber().toLowerCase().contains(lowerKeyword)
                || document.getCustomerName().toLowerCase().contains(lowerKeyword)
                || document.getProductCode().toLowerCase().contains(lowerKeyword);
    }

    private boolean matchStatus(QuoteDocument document, String status) {
        if (!StringUtils.hasText(status)) {
            return true;
        }

        return document.getStatus().equalsIgnoreCase(status);
    }

    private QuoteListItemResponse toListItemResponse(QuoteDocument document) {
        return new QuoteListItemResponse(
                document.getId(),
                document.getQuoteNumber(),
                document.getCustomerName(),
                document.getProductCode(),
                document.getPremium(),
                document.getStatus()
        );
    }
}
