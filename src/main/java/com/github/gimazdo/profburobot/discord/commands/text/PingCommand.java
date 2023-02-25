package com.github.gimazdo.profburobot.discord.commands.text;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.component.*;
import discord4j.core.object.entity.Message;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class PingCommand implements Command {

    @Override
    public Mono<Void> command(MessageCreateEvent event) {
        Message message = event.getMessage();
        log.info("New event {}", event);
        log.info("New message {}", message);
        log.info("Message content {}", message.getContent());
        if (message.getContent().equalsIgnoreCase("!ping")) {
            return message.getChannel()
                    .flatMap(channel ->
                            channel.createMessage("pong!"))
                    .then();
        }

        return Mono.empty();
    }
}
