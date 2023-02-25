package com.github.gimazdo.profburobot.services;

import com.github.gimazdo.profburobot.entity.TempChannel;
import com.github.gimazdo.profburobot.repository.TempChannelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Service
public class TempChannelService {

    private final TempChannelRepository tempChannelRepository;


    public Flux<TempChannel> getExpired(){
        return tempChannelRepository.findByExpiredDateBefore(LocalDateTime.now());
    }

    public Long getTotalCount(){
        return tempChannelRepository.countAll().blockFirst();
    }
    public Mono<TempChannel> save(TempChannel tempChannel){
        return tempChannelRepository.save(tempChannel);
    }

    public Mono<Void> deleteById(Integer id){
        return tempChannelRepository.deleteById(id);
    }


}
