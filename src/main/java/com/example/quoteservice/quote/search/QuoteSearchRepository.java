package com.example.quoteservice.quote.search;

import com.example.quoteservice.quote.document.QuoteDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface QuoteSearchRepository
        extends ElasticsearchRepository<QuoteDocument, String> {
}
