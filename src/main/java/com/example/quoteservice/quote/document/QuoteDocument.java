package com.example.quoteservice.quote.document;
import org.springframework.data.annotation.Id;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(indexName = "quote_index")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuoteDocument {

    @Id
    private String id;

    private String quoteNumber;

    private String customerName;

    private String productCode;

    private BigDecimal premium;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
