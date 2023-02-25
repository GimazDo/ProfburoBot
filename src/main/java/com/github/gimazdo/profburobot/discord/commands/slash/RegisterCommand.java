package com.github.gimazdo.profburobot.discord.commands.slash;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.TextInput;
import discord4j.core.spec.InteractionPresentModalSpec;
import discord4j.discordjson.json.ApplicationCommandRequest;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

import static com.github.gimazdo.profburobot.util.Constants.Discord.REGISTER_MODAL;


public class RegisterCommand implements SlashCommand {

    private static final String CHAT_INPUT_COMMAND_NAME = "register";

    @Override
    public Mono<Void> command(ChatInputInteractionEvent event) {
        TextInput login = TextInput.small("login", "Логин", "Введите логин ЛК");
        TextInput password = TextInput.small("password", "Пароль", "Введите пароль");
        if (CHAT_INPUT_COMMAND_NAME.equals(event.getCommandName())) {
            return event.presentModal(InteractionPresentModalSpec.builder()
                    .title("Регистрация")
                    .customId(REGISTER_MODAL)
                    .addComponent(ActionRow.of(login))
                    .addComponent(ActionRow.of(password))
                    .build());
        }
        return Mono.empty();
    }

    @Override
    public ApplicationCommandRequest getName() {
        return ApplicationCommandRequest.builder()
                .name(CHAT_INPUT_COMMAND_NAME)
                .description("Регистрация через ЛК ГУАП")
                .build();
    }
}
