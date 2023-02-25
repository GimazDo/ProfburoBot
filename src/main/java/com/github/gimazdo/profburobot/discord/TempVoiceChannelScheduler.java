package com.github.gimazdo.profburobot.discord;

import com.github.gimazdo.profburobot.entity.TempChannel;
import com.github.gimazdo.profburobot.services.TempChannelService;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@EnableScheduling
@RequiredArgsConstructor
@Slf4j
@Service

public class TempVoiceChannelScheduler {
    private final TempChannelService tempChannelService;
    private final DiscordClient client;

    @Scheduled(fixedRate = 100000L)
    private void checkDeleteChannel() {
        log.info("Start to check channel for delete");
        Flux<TempChannel> tempChannels = tempChannelService.getExpired();
        Mono<?> mono = Mono.empty();
        mono = mono.and(tempChannels.flatMap(tempChannel ->
        {
            Mono<?> monos = Mono.empty();
            monos = monos.and(client
                    .getChannelById(Snowflake.of(tempChannel.getDiscordId()))
                    .delete("Истек срок работы"));
            monos = monos.and(tempChannelService.deleteById(tempChannel.getId()));
            return monos;
        }));
        mono.block();
    }
}
