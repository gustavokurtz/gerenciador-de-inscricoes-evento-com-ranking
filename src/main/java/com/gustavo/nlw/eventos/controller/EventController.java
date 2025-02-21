package com.gustavo.nlw.eventos.controller;

import com.gustavo.nlw.eventos.model.Event;
import com.gustavo.nlw.eventos.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/events")
    public Event addNewEvent(@RequestBody Event newEvent){
        return eventService.addNewEvent(newEvent);
    }

    @GetMapping("/events")
    public List<Event> listEvents(){
        return eventService.getAllEvents();
    }

    @GetMapping("/events/{prettyName}")
    public ResponseEntity<Event> getEventByPrettyName(@PathVariable String prettyName){

        Event evt = eventService.getByPrettyName(prettyName);

        if(evt == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(evt);

    }



}
