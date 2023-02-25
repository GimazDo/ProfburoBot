package com.github.gimazdo.profburobot.discord.commands.slash;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandRequest;
import reactor.core.publisher.Mono;

public interface SlashCommand {
    Mono<Void> command(ChatInputInteractionEvent event);

    ApplicationCommandRequest getName();
}
