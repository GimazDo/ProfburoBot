package com.github.gimazdo.profburobot.repository;

import com.github.gimazdo.profburobot.entity.TempChannel;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Repository
public interface TempChannelRepository extends ReactiveCrudRepository<TempChannel, Integer> {
    Flux<TempChannel> findByExpiredDateBefore(LocalDateTime localDateTime);
    @Query("SELECT COUNT(t) FROM temp_channel t")
    Flux<Long> countAll();
}
