package com.example.quoteservice.quote.messaging;

import com.example.quoteservice.quote.service.QuoteIndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class QuoteSyncEsConsumer {

    private final QuoteIndexService quoteIndexService;

    private static final Logger log = LoggerFactory.getLogger(QuoteSyncEsConsumer.class);

    public QuoteSyncEsConsumer(QuoteIndexService quoteIndexService) {
        this.quoteIndexService = quoteIndexService;
    }

    @RabbitListener(queues = QuoteRabbitConfig.QUOTE_SYNC_ES_QUEUE)
    public void consume(QuoteSyncMessage message) {

        log.info("Consuming quote sync ES message: quoteId={}, reason={}",
                message.getQuoteId(),
                message.getReason());

        quoteIndexService.syncQuote(message.getQuoteId());
    }
}
