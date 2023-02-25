package com.github.gimazdo.profburobot.discord.commands.modals;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import reactor.core.publisher.Mono;

public interface ModalResolver {
    Mono<Void> command(ModalSubmitInteractionEvent event);
}
