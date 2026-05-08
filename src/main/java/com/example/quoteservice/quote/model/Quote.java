package com.example.quoteservice.quote.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quote {
    private String id;
    private String quoteNumber;
    private String customerName;//Tên khách hàng
    private String productCode;//Mã sản phẩm
    private BigDecimal premium;//Phí
    private QuoteStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

