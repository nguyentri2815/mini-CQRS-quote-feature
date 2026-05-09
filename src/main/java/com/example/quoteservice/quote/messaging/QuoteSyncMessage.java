package com.example.quoteservice.quote.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuoteSyncMessage {

    private String quoteId;

    private String reason;
}
