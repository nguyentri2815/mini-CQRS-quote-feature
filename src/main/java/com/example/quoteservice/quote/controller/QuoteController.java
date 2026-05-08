package com.example.quoteservice.quote.controller;

import com.example.quoteservice.quote.dto.QuoteCreateRequest;
import com.example.quoteservice.quote.dto.QuoteDetailResponse;
import com.example.quoteservice.quote.dto.QuoteListItemResponse;
import com.example.quoteservice.quote.dto.QuoteResponse;
import com.example.quoteservice.quote.service.QuoteCommandService;
import com.example.quoteservice.quote.service.QuoteQueryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quotes")
public class QuoteController {

    private final QuoteCommandService quoteCommandService;

    private final QuoteQueryService quoteQueryService;

    public QuoteController(
            QuoteCommandService quoteCommandService,
            QuoteQueryService quoteQueryService
    ) {
        this.quoteCommandService = quoteCommandService;
        this.quoteQueryService = quoteQueryService;
    }

    @PostMapping
    public QuoteResponse create(@Valid @RequestBody QuoteCreateRequest request) {
        return quoteCommandService.create(request);
    }

    @PostMapping("/{id}/submit")
    public QuoteResponse submit(@PathVariable String id) {
        return quoteCommandService.submit(id);
    }

    @PostMapping("/{id}/approve")
    public QuoteResponse approve(@PathVariable String id) {
        return quoteCommandService.approve(id);
    }

    @PostMapping("/{id}/reject")
    public QuoteResponse reject(@PathVariable String id) {
        return quoteCommandService.reject(id);
    }

    @GetMapping("/{id}")
    public QuoteDetailResponse detail(@PathVariable String id) {
        return quoteQueryService.detail(id);
    }

    @GetMapping
    public List<QuoteListItemResponse> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status
    ) {
        return quoteQueryService.list(keyword, status);
    }
}
