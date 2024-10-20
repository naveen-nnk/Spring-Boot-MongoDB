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
import com.responsive.naveen.assignment.demo.model.TradingUsers;
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

    @GetMapping("/users")
    public List < TradingUsers > getAllUsers() {
        return tradingRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity < TradingUsers > getUserById(@PathVariable(value = "id") Long userId)
    throws ResourceNotFoundException {
    	TradingUsers users = tradingRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
        return ResponseEntity.ok().body(users);
    }

    @PostMapping("/users")
    public TradingUsers createUser(@Valid @RequestBody TradingUsers user) {
        user.setId(sequenceGeneratorService.generateSequence(TradingUsers.SEQUENCE_NAME));
        return tradingRepository.save(user);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity < TradingUsers > updateUser(@PathVariable(value = "id") Long userId,
        @Valid @RequestBody TradingUsers userDetails) throws ResourceNotFoundException {
    	TradingUsers user = tradingRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));

        user.setEmailId(userDetails.getEmailId());
        user.setLastName(userDetails.getLastName());
        user.setFirstName(userDetails.getFirstName());
        final TradingUsers updatedUser = tradingRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/users/{id}")
    public Map < String, Boolean > deleteUser(@PathVariable(value = "id") Long userId)
    throws ResourceNotFoundException {
    	TradingUsers user = tradingRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));

        tradingRepository.delete(user);
        Map < String, Boolean > response = new HashMap < > ();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}