package com.gustavo.nlw.eventos.repo;

import com.gustavo.nlw.eventos.dto.SubscriptionRankingItem;
import com.gustavo.nlw.eventos.model.Event;
import com.gustavo.nlw.eventos.model.Subscription;
import com.gustavo.nlw.eventos.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscriptionRepo extends CrudRepository<Subscription, Integer> {
    public Subscription findByEventAndSubscriber(Event evt, User user);

    List<Subscription> findByEventPrettyName(String prettyName);

    @Query(value = "SELECT COUNT(*) AS quantidade, s.indication_user_id, u.user_name " +
            "FROM tbl_subscription s " +
            "INNER JOIN tbl_user u ON s.indication_user_id = u.user_id " +
            "WHERE s.indication_user_id IS NOT NULL " +
            "AND s.event_id = :eventId " +
            "GROUP BY s.indication_user_id, u.user_name " +  // PostgreSQL exige todas as colunas do SELECT no GROUP BY
            "ORDER BY quantidade DESC", nativeQuery = true)
    public List<SubscriptionRankingItem> generateRanking(@Param("eventId") Integer eventId);


}
