package com.gustavo.nlw.eventos.controller;

import com.gustavo.nlw.eventos.dto.ErrorMessage;
import com.gustavo.nlw.eventos.dto.SubscriptionResponse;
import com.gustavo.nlw.eventos.exception.EventNotFoundException;
import com.gustavo.nlw.eventos.exception.SubscriptionConflictException;
import com.gustavo.nlw.eventos.exception.UserIndicadorNotFoundException;
import com.gustavo.nlw.eventos.model.Subscription;
import com.gustavo.nlw.eventos.model.User;
import com.gustavo.nlw.eventos.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SubscriptionController {

    @Autowired
    private SubscriptionService service;

    @PostMapping({"/subscription/{prettyName}", "/subscription/{prettyName}/{userId}"})
    public ResponseEntity<?> createSubscription(@PathVariable String prettyName, @RequestBody User subscriber, @PathVariable(required = false) Integer userId){
        try{
            SubscriptionResponse res = service.createNewSubscription(prettyName, subscriber, userId);
            if (res != null){
                return ResponseEntity.ok(res);
            }

        }catch(EventNotFoundException ex){
            return ResponseEntity.status(404).body(new ErrorMessage(ex.getMessage()));
        }
        catch(SubscriptionConflictException ex){
            return ResponseEntity.status(409).body(new ErrorMessage(ex.getMessage()));
        }
        catch(UserIndicadorNotFoundException ex){
            return ResponseEntity.status(404).body(new ErrorMessage(ex.getMessage()));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/subscription/{prettyName}/ranking")
    public ResponseEntity<?> generateRankingByEvent(@PathVariable String prettyName){
        try{
            return ResponseEntity.ok(service.getCompleteRanking(prettyName).subList(0, 3));
        } catch (EventNotFoundException e){
            return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
        }
    }


    @GetMapping("subscription/{prettyName}/ranking/{userId}")
    public ResponseEntity<?> generateRankingByEventAndUser(@PathVariable String prettyName, @PathVariable Integer userId){

        try{
            return ResponseEntity.ok(service.getRankingByUser(prettyName, userId));
        } catch (Exception e){
            return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
        }
    }

}
