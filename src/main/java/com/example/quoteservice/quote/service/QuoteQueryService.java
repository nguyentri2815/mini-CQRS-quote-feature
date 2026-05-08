package com.example.quoteservice.quote.service;

import com.example.quoteservice.quote.dto.QuoteDetailResponse;
import com.example.quoteservice.quote.dto.QuoteListItemResponse;
import com.example.quoteservice.quote.mapper.QuoteMapper;
import com.example.quoteservice.quote.model.Quote;
import com.example.quoteservice.quote.model.QuoteStatus;
import com.example.quoteservice.quote.service.QuoteCommandService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;

@Service
public class QuoteQueryService {

    private final QuoteCommandService quoteCommandService;

    private final QuoteMapper quoteMapper;

    public QuoteQueryService(
            QuoteCommandService quoteCommandService,
            QuoteMapper quoteMapper
    ) {
        this.quoteCommandService = quoteCommandService;
        this.quoteMapper = quoteMapper;
    }

    public QuoteDetailResponse detail(String id) {
        Quote quote = quoteCommandService.findQuote(id);

        return quoteMapper.toDetailResponse(quote);
    }

    public List<QuoteListItemResponse> list(String keyword, String status) {
        return quoteCommandService.getQuoteStore()
                .values()
                .stream()
                .filter(quote -> matchKeyword(quote, keyword))
                .filter(quote -> matchStatus(quote, status))
                .sorted(Comparator.comparing(Quote::getCreatedAt).reversed())
                .map(quoteMapper::toListItemResponse)
                .toList();
    }

    private boolean matchKeyword(Quote quote, String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return true;
        }

        String lowerKeyword = keyword.toLowerCase();

        return quote.getQuoteNumber().toLowerCase().contains(lowerKeyword)
                || quote.getCustomerName().toLowerCase().contains(lowerKeyword)
                || quote.getProductCode().toLowerCase().contains(lowerKeyword);
    }

    private boolean matchStatus(Quote quote, String status) {
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
}
