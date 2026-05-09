package com.example.quoteservice.quote.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuoteRabbitConfig {

    public static final String QUOTE_EXCHANGE = "quote.exchange";

    public static final String QUOTE_SYNC_ES_QUEUE = "quote.sync-es.queue";

    public static final String QUOTE_SYNC_ES_ROUTING_KEY = "quote.sync-es";

    @Bean
    public TopicExchange quoteExchange() {
        return new TopicExchange(QUOTE_EXCHANGE);
    }

    @Bean
    public Queue quoteSyncEsQueue() {
        return new Queue(QUOTE_SYNC_ES_QUEUE, true);
    }

    @Bean
    public Binding quoteSyncEsBinding() {
        return BindingBuilder
                .bind(quoteSyncEsQueue())
                .to(quoteExchange())
                .with(QUOTE_SYNC_ES_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
