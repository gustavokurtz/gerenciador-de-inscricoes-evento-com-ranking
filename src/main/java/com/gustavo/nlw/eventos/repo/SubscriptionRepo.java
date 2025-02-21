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

    @Query(value = "SELECT COUNT(subscription_number) AS quantidade, indication_user_id, user_name " +
            "FROM tbl_subscription INNER JOIN tbl_user " +
            "ON tbl_subscription.indication_user_id = tbl_user.user_id " +
            "WHERE indication_user_id IS NOT NULL " +
            "AND event_id = :eventId " +
            "GROUP BY indication_user_id " +
            "ORDER BY quantidade DESC", nativeQuery = true)
    public List<SubscriptionRankingItem> generateRanking(@Param("eventId") Integer eventId);

}
