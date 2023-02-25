package com.github.gimazdo.profburobot.discord.commands.slash;

import com.github.gimazdo.profburobot.entity.TempChannel;
import com.github.gimazdo.profburobot.services.TempChannelService;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.core.spec.VoiceChannelCreateSpec;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class CreateTempChannel implements SlashCommand{
    private static final String CHAT_INPUT_COMMAND_NAME = "ctc";
    private final TempChannelService tempChannelService;

    @Override
    public Mono<Void> command(ChatInputInteractionEvent event) {
        if(!event.getCommandName().equals(CHAT_INPUT_COMMAND_NAME)) return Mono.empty();
        if (tempChannelService.getTotalCount()>=10)
           return event.reply("Превышено кол-во временных каналов. Одновременно их может быть не больше 10");
        long time = event.getOption("time").get().getValue().get().asLong();
        if(time < 0 || time > 24) return event.reply("Неверно задан параметр");
        CompletableFuture.runAsync(() -> createTempChannel(event));

        return  event.reply("Временный канал будет создан в ближайшее время").then();
    }

    @Override
    public ApplicationCommandRequest getName() {
        return ApplicationCommandRequest.builder()
                .name(CHAT_INPUT_COMMAND_NAME)
                .description("Создание временного канала на 24 часа")
                .addOption(ApplicationCommandOptionData.builder()
                        .required(true)
                        .name("name")
                        .description("Название канала")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .build())
                .addOption(ApplicationCommandOptionData.builder()
                        .required(true)
                        .name("time")
                        .description("Время жизни канала, не может быть больше 24 и меньше 1")
                        .type(ApplicationCommandOption.Type.INTEGER.getValue())
                        .build())
                .build();
    }

    private void createTempChannel(ChatInputInteractionEvent event){
        String name = event.getOption("name").get().getValue().get().asString();
        long time = event.getOption("time").get().getValue().get().asLong();
        VoiceChannel o = event.getInteraction().getGuild()
                .flatMap(guild -> guild.createVoiceChannel(VoiceChannelCreateSpec.builder()
                        .name(name)
                        .build())).block();
        TempChannel tempChannel = new TempChannel();
        tempChannel.setExpiredDate(LocalDateTime.now().plusHours(time));
        tempChannel.setDiscordId(o.getId().asLong());
        tempChannelService.save(tempChannel).block();
        System.out.println(o);
    }

}
