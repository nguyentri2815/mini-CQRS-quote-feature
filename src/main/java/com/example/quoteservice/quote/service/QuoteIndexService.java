package com.example.quoteservice.quote.service;

import com.example.quoteservice.common.exception.NotFoundException;
import com.example.quoteservice.quote.document.QuoteDocument;
import com.example.quoteservice.quote.entity.QuoteEntity;
import com.example.quoteservice.quote.mapper.QuoteSearchMapper;
import com.example.quoteservice.quote.repository.QuoteRepository;
import com.example.quoteservice.quote.search.QuoteSearchRepository;
import org.springframework.stereotype.Service;

@Service
public class QuoteIndexService {

    private final QuoteRepository quoteRepository;
    private final QuoteSearchRepository quoteSearchRepository;
    private final QuoteSearchMapper quoteSearchMapper;

    public QuoteIndexService(
            QuoteRepository quoteRepository,
            QuoteSearchRepository quoteSearchRepository,
            QuoteSearchMapper quoteSearchMapper
    ) {
        this.quoteRepository = quoteRepository;
        this.quoteSearchRepository = quoteSearchRepository;
        this.quoteSearchMapper = quoteSearchMapper;
    }

    public void syncQuote(String quoteId) {
        QuoteEntity quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new NotFoundException("Quote not found: " + quoteId));

        QuoteDocument document = quoteSearchMapper.toDocument(quote);

        quoteSearchRepository.save(document);
    }
}
