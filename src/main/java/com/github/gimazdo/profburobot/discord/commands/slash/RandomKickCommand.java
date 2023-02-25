package com.github.gimazdo.profburobot.discord.commands.slash;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.GuildMemberEditSpec;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.discordjson.possible.Possible;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.Random;

public class RandomKickCommand implements SlashCommand {

    private static final String CHAT_INPUT_COMMAND_NAME = "roll-kick";

    @Override
    public Mono<Void> command(ChatInputInteractionEvent event) {
        if (!event.getCommandName().equals(CHAT_INPUT_COMMAND_NAME))
            return Mono.empty();
        return event.deferReply().then(chooseWhoKick(event));

    }

    private Mono<Void> chooseWhoKick(ChatInputInteractionEvent event) {
        return event.getInteraction().getMember().orElseThrow(() -> new RuntimeException("Нет юзера"))
                .getVoiceState()
                .flatMap(voiceState -> voiceState.getChannel().flatMap(voiceChannel ->
                        voiceChannel.getVoiceStates().buffer(Duration.ofMillis(2000))
                                .flatMap(list -> {
                                    Random random = new Random(Instant.now().getEpochSecond());
                                    int kick = random.nextInt(list.size()+1);
                                    return list.get(kick).getMember().flatMap(member ->
                                            member
                                                    .edit(GuildMemberEditSpec
                                                            .builder()
                                                            .communicationDisabledUntil(Possible.of(Optional.of(Instant.now().plusSeconds(30L))))
                                                            .reason("Избран рандомом")
                                                            .build())
                                                    .and(event.createFollowup(member.getDisplayName() + " не повезло")));
                                }).then())).then();
    }

    @Override
    public ApplicationCommandRequest getName() {
        return ApplicationCommandRequest.builder()
                .name(CHAT_INPUT_COMMAND_NAME)
                .description("Тест")
                .build();
    }
}
