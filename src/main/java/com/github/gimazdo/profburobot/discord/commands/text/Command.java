package com.github.gimazdo.profburobot.discord.commands.text;

import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

public interface Command {
    Mono<Void> command(MessageCreateEvent event);
}
