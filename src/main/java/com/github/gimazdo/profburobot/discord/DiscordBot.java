package com.github.gimazdo.profburobot.discord;

import com.github.gimazdo.profburobot.discord.commands.modals.ModalFabric;
import com.github.gimazdo.profburobot.discord.commands.modals.ModalResolver;
import com.github.gimazdo.profburobot.discord.commands.slash.SlashCommand;
import com.github.gimazdo.profburobot.discord.commands.slash.SlashCommandFabric;
import com.github.gimazdo.profburobot.discord.commands.text.Command;
import com.github.gimazdo.profburobot.discord.commands.text.CommandFabric;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.discordjson.json.*;
import discord4j.discordjson.possible.Possible;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.uri.Uri;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
@EnableScheduling
public class DiscordBot {


    private final CommandFabric commandFabric;
    private final SlashCommandFabric slashCommandFabric;
    private final ModalFabric modalFabric;
    private final DiscordClient client;
    private final Long guildId;

    public DiscordBot(DiscordClient discordClient,
                      @Value("${discord.guild-id}") Long guildId,
                      CommandFabric commandFabric,
                      SlashCommandFabric slashCommandFabric,
                      ModalFabric modalFabric) {
        this.guildId = guildId;
        this.modalFabric = modalFabric;
        this.commandFabric = commandFabric;
        this.slashCommandFabric = slashCommandFabric;
        client = discordClient;

        CompletableFuture.runAsync(this::startBot);
    }

    private void startBot(){
        Mono<?> mono = client.withGateway(client -> {
            Mono<?> monos = Mono.empty();
            List<ApplicationCommandRequest> commands = new ArrayList<>();
            for (SlashCommand slashCommand : slashCommandFabric.getAll()) {
                commands.add(slashCommand.getName());
                monos = monos.and(client.on(
                                ChatInputInteractionEvent.class,
                                slashCommand::command)
                        .then());
            }
            for (ModalResolver modalResolver : modalFabric.getAll()) {
                monos = monos.and(client.on(
                        ModalSubmitInteractionEvent.class,
                        modalResolver::command));
            }
            return GuildCommandRegistrar.create(client.getRestClient(), guildId, commands)
                    .registerCommands()
                    .thenMany(monos);
        });
        for (Command command : commandFabric.getAllCommands()) {
            mono = mono.and(client.withGateway((GatewayDiscordClient gateway) ->
                    gateway.on(MessageCreateEvent.class,
                                    command::command)
                            .then()));
        }

        mono.block();
    }

    public void sendMessage(Long chatId,String text, List<String> photos){
        Mono<?> mono = client.getChannelById(Snowflake.of(chatId))
                .createMessage(text + "\n" + String.join("\n", photos));
        mono.subscribe();
    }
}
