package com.responsive.naveen.assignment.demo.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.responsive.naveen.assignment.demo.exception.ResourceNotFoundException;
import com.responsive.naveen.assignment.demo.model.TradingStocks;
import com.responsive.naveen.assignment.demo.repository.TradingRepository;
import com.responsive.naveen.assignment.demo.service.SequenceGeneratorService;

import jakarta.validation.Valid;



@CrossOrigin(origins = "http://localhost:3001")
@RestController
@RequestMapping("/api/v1")
public class TradingController {
    @Autowired
    private TradingRepository tradingRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @GetMapping("/stocks")
    public List < TradingStocks > getAllStocks() {
        return tradingRepository.findAll();
    }

    @GetMapping("/stocks/{id}")
    public ResponseEntity < TradingStocks > getStockById(@PathVariable(value = "id") Long stockId)
    throws ResourceNotFoundException {
    	TradingStocks Stocks = tradingRepository.findById(stockId)
            .orElseThrow(() -> new ResourceNotFoundException("Stock not found for this id :: " + stockId));
        return ResponseEntity.ok().body(Stocks);
    }

    @PostMapping("/stocks")
    public TradingStocks createStock(@Valid @RequestBody TradingStocks stock) {
        stock.setId(sequenceGeneratorService.generateSequence(TradingStocks.SEQUENCE_NAME));
        return tradingRepository.save(stock);
    }

    @PutMapping("/stocks/{id}")
    public ResponseEntity < TradingStocks > updateStock(@PathVariable(value = "id") Long stockId,
        @Valid @RequestBody TradingStocks stockDetails) throws ResourceNotFoundException {
    	TradingStocks stock = tradingRepository.findById(stockId)
            .orElseThrow(() -> new ResourceNotFoundException("Stock not found for this id :: " + stockId));

        stock.setEmailId(stockDetails.getEmailId());
        stock.setQuantity(stockDetails.getQuantity());
        stock.setStockName(stockDetails.getStockName());
        final TradingStocks updatedStock = tradingRepository.save(stock);
        return ResponseEntity.ok(updatedStock);
    }

    @DeleteMapping("/stocks/{id}")
    public Map < String, Boolean > deleteStock(@PathVariable(value = "id") Long stockId)
    throws ResourceNotFoundException {
    	TradingStocks stock = tradingRepository.findById(stockId)
            .orElseThrow(() -> new ResourceNotFoundException("Stock not found for this id :: " + stockId));

        tradingRepository.delete(stock);
        Map < String, Boolean > response = new HashMap < > ();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}