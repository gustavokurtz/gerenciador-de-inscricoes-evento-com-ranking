package com.gustavo.nlw.eventos.service;


import com.gustavo.nlw.eventos.dto.SubscribersResponse;
import com.gustavo.nlw.eventos.dto.SubscriptionRankingByUser;
import com.gustavo.nlw.eventos.dto.SubscriptionRankingItem;
import com.gustavo.nlw.eventos.dto.SubscriptionResponse;
import com.gustavo.nlw.eventos.exception.EventNotFoundException;
import com.gustavo.nlw.eventos.exception.SubscriptionConflictException;
import com.gustavo.nlw.eventos.exception.UserIndicadorNotFoundException;
import com.gustavo.nlw.eventos.model.Event;
import com.gustavo.nlw.eventos.model.Subscription;
import com.gustavo.nlw.eventos.model.User;
import com.gustavo.nlw.eventos.repo.EventRepo;
import com.gustavo.nlw.eventos.repo.SubscriptionRepo;
import com.gustavo.nlw.eventos.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class SubscriptionService {

    @Autowired
    private EventRepo evtRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SubscriptionRepo subsRepo;
    @Autowired
    private EventService eventService;
    @Autowired
    private EventRepo eventRepo;

    public SubscriptionResponse createNewSubscription(String eventName, User user, Integer userId){
        Subscription subscription = new Subscription();

        Event evt = evtRepo.findByPrettyName(eventName);

        if(evt == null){
            throw new EventNotFoundException("Evento "+eventName+ " não existe");
        }

        User userRec = userRepo.findByEmail(user.getEmail());
        if(userRec == null) {
            userRec = userRepo.save(user);
        }


        User indicador = null;
        if(userId != null){
            indicador = userRepo.findById(userId).orElse(null);
            if(indicador == null){
                throw new UserIndicadorNotFoundException("Usuario "+userId+ " indicador não existe ");
            }
        }



        subscription.setEvent(evt);
        subscription.setSubscriber(userRec);
        subscription.setIndication(indicador);

        Subscription tmpSub = subsRepo.findByEventAndSubscriber(evt, userRec);
        if(tmpSub != null) {
            throw new SubscriptionConflictException("Ja existe inscrição para o usuário "+ userRec.getName()+ " no evento "+evt.getTitle());



        }




        Subscription res = subsRepo.save(subscription);

        return new SubscriptionResponse(res.getSubscriptionNumber(), "https://revinfinity.pro/subscription/"+res.getEvent().getPrettyName()+"/"+res.getSubscriber().getId());

    }


    public List<SubscribersResponse> getAllSubscribers(String prettyName) {
        List<Subscription> subs = (List<Subscription>) subsRepo.findByEventPrettyName(prettyName);

        return subs.stream()
                .map(sub -> new SubscribersResponse(sub.getSubscriber().getName(), sub.getEvent().getTitle(), sub.getEvent().getPrettyName()))
                .collect(Collectors.toList());
    }




    public List<SubscriptionRankingItem> getCompleteRanking(String prettyName){
        Event evt = evtRepo.findByPrettyName(prettyName);
        if(evt == null){
            throw new EventNotFoundException("Ranking do evento "+prettyName+" não existe!");
        }
        return subsRepo.generateRanking(evt.getEventId());
    }

    public SubscriptionRankingByUser getRankingByUser(String prettyName, Integer userId){

        List<SubscriptionRankingItem> ranking = getCompleteRanking(prettyName);

        SubscriptionRankingItem item = ranking.stream().filter(i->i.userId().equals(userId)).findFirst().orElse(null);

        if(item == null){
            throw new UserIndicadorNotFoundException("Não há inscrições com indicação do usuario "+userId);


        }
        Integer posicao = IntStream.range(0, ranking.size()).filter(pos -> ranking.get(pos).userId().equals(userId)).findFirst().getAsInt();

        return new SubscriptionRankingByUser(item, posicao+1);
    }

}
