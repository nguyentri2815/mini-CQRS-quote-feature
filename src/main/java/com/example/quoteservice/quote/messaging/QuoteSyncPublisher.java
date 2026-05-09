package com.example.quoteservice.quote.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class QuoteSyncPublisher {

    private final RabbitTemplate rabbitTemplate;

    private static final Logger log = LoggerFactory.getLogger(QuoteSyncPublisher.class);

    public QuoteSyncPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishSyncEs(String quoteId, String reason) {
        QuoteSyncMessage message = new QuoteSyncMessage(quoteId, reason);

        log.info("Publishing quote sync ES message: quoteId={}, reason={}", quoteId, reason);

        rabbitTemplate.convertAndSend(
                QuoteRabbitConfig.QUOTE_EXCHANGE,
                QuoteRabbitConfig.QUOTE_SYNC_ES_ROUTING_KEY,
                message
        );
    }
}
