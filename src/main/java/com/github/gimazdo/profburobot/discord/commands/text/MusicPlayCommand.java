package com.github.gimazdo.profburobot.discord.commands.text;

import com.github.gimazdo.profburobot.discord.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@RequiredArgsConstructor
public class MusicPlayCommand implements Command {

    private final AudioPlayerManager playerManager;
    private final TrackScheduler scheduler;

    @Override
    public Mono<Void> command(MessageCreateEvent event) {
        Message message = event.getMessage();
        if (message.getContent().startsWith("!play"))
            return Mono.justOrEmpty(event.getMessage().getContent())
                    .map(content -> Arrays.asList(content.split(" ")))
                    .doOnNext(command -> playerManager.loadItem(command.get(1), scheduler))
                    .then();
        return Mono.empty();
    }
}
