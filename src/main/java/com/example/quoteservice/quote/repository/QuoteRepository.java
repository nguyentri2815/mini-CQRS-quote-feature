package com.example.quoteservice.quote.repository;

import com.example.quoteservice.quote.entity.QuoteEntity;
import com.example.quoteservice.quote.model.QuoteStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuoteRepository extends JpaRepository<QuoteEntity, String> {
    Optional<QuoteEntity> findByQuoteNumber(String quoteNumber);

    List<QuoteEntity> findByStatus(QuoteStatus status);

}

