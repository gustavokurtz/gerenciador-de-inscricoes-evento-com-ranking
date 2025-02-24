package com.gustavo.nlw.eventos.controller;

import com.gustavo.nlw.eventos.dto.ErrorMessage;
import com.gustavo.nlw.eventos.dto.SubscriptionResponse;
import com.gustavo.nlw.eventos.exception.EventNotFoundException;
import com.gustavo.nlw.eventos.exception.SubscriptionConflictException;
import com.gustavo.nlw.eventos.exception.UserIndicadorNotFoundException;
import com.gustavo.nlw.eventos.model.User;
import com.gustavo.nlw.eventos.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class SubscriptionController {

    @Autowired
    private SubscriptionService service;

    @PostMapping({"/subscription/{prettyName}", "/subscription/{prettyName}/{userId}"})
    public ResponseEntity<?> createSubscription(@PathVariable String prettyName,
                                                @RequestBody User subscriber,
                                                @PathVariable(required = false) Integer userId) {
        try {
            SubscriptionResponse res = service.createNewSubscription(prettyName, subscriber, userId);
            return ResponseEntity.ok(res);
        } catch (EventNotFoundException ex) {
            return ResponseEntity.status(404).body(new ErrorMessage(ex.getMessage()));
        } catch (SubscriptionConflictException ex) {
            return ResponseEntity.status(409).body(new ErrorMessage(ex.getMessage()));
        } catch (UserIndicadorNotFoundException ex) {
            return ResponseEntity.status(404).body(new ErrorMessage(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ErrorMessage("Erro inesperado: " + ex.getMessage()));
        }
    }

    @GetMapping("/subscription/{prettyName}/ranking")
    public ResponseEntity<?> generateRankingByEvent(@PathVariable String prettyName) {
        try {
            List<?> ranking = service.getCompleteRanking(prettyName);

            // Garantindo que o tamanho da sublista nunca seja maior do que a lista original
            List<?> top3 = ranking.subList(0, Math.min(3, ranking.size()));

            return ResponseEntity.ok(top3);
        } catch (EventNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorMessage("Erro inesperado: " + e.getMessage()));
        }
    }

    @GetMapping("subscription/{prettyName}/ranking/{userId}")
    public ResponseEntity<?> generateRankingByEventAndUser(@PathVariable String prettyName,
                                                           @PathVariable Integer userId) {
        try {
            return ResponseEntity.ok(service.getRankingByUser(prettyName, userId));
        } catch (UserIndicadorNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
        } catch (EventNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorMessage("Erro inesperado: " + e.getMessage()));
        }
    }
}
