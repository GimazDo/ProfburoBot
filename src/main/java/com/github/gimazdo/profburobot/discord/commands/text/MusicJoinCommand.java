package com.github.gimazdo.profburobot.discord.commands.text;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.voice.AudioProvider;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class MusicJoinCommand implements Command {

    private final AudioProvider audioProvider;

    @Override
    public Mono<Void> command(MessageCreateEvent event) {
        Message message = event.getMessage();
        if (message.getContent().equalsIgnoreCase("!join"))
            return Mono.justOrEmpty(event.getMember())
                    .flatMap(Member::getVoiceState)
                    .flatMap(VoiceState::getChannel)
                    .flatMap(channel -> channel.join(spec -> spec.setProvider(audioProvider)))
                    .then();
        return Mono.empty();
    }
}
