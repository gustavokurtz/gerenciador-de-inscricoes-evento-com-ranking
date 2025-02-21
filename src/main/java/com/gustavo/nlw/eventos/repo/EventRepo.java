package com.gustavo.nlw.eventos.repo;

import com.gustavo.nlw.eventos.model.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepo extends CrudRepository<Event, Integer> {
    public Event findByPrettyName(String pretyName);
}
