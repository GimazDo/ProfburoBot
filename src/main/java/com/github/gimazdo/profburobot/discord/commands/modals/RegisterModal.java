package com.github.gimazdo.profburobot.discord.commands.modals;

import com.github.gimazdo.profburobot.dto.SUAIUSer;
import com.github.gimazdo.profburobot.services.SUAIClient;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import discord4j.core.object.component.TextInput;
import discord4j.core.spec.GuildMemberEditSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.discordjson.possible.Possible;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.github.gimazdo.profburobot.util.Constants.Discord.REGISTER_MODAL;


@RequiredArgsConstructor
@Slf4j
public class RegisterModal implements ModalResolver{
    private final Long registredMemberRoleId;

    private final Long newMemberRoleId;
    private final SUAIClient suaiClient;
    @Override
    public Mono<Void> command(ModalSubmitInteractionEvent event) {
        if (REGISTER_MODAL.equals(event.getCustomId())) {

            final String password_ = event.getComponents(TextInput.class).stream()
                    .filter(component -> component.getCustomId().equals("password"))
                    .findFirst().get().getValue().get();

            final String login_ = event.getComponents(TextInput.class).stream()
                    .filter(component -> component.getCustomId().equals("login"))
                    .findFirst().get().getValue().get();

            CompletableFuture.runAsync(() -> setNick(event, login_, password_));
            return event.reply(InteractionApplicationCommandCallbackSpec.builder().build()).and(event.getInteraction().getMember().get().getPrivateChannel().flatMap(
                    privateChannel -> privateChannel.createMessage("Данные приняты, по итогам авторизации вы получите сообщение.")
            )).then();
        }
        return event.reply(InteractionApplicationCommandCallbackSpec.builder().build()).and(event.getInteraction().getMember().get().getPrivateChannel().flatMap(
                privateChannel -> privateChannel.createMessage("Что-то пошло не так... Попробуйте снова")
        ).then());
    }

    public void setNick(ModalSubmitInteractionEvent event, String login, String password) {
        try {
            SUAIUSer user = suaiClient.getUserInfo(login, password);
            String name = user.getFirstname() + " " + user.getLastname();
            Mono<?> mono =
                    event.getInteraction()
                            .getMember()
                            .get()
                            .edit(GuildMemberEditSpec
                                    .create()
                                    .withNickname(Possible.of(Optional.of(name))));
            mono = mono.and(event.getInteraction().getMember().get().getPrivateChannel().flatMap(privateChannel ->
                            privateChannel.createMessage("Авторизация прошла успешно")))
                    .and(event.getInteraction().getMember()
                            .get()
                            .removeRole(Snowflake.of(newMemberRoleId)).and(
                                    event.getInteraction().getMember()
                                            .get().addRole(Snowflake.of(registredMemberRoleId))
                            ));
            mono.block();
        } catch (Exception e) {
            log.info("Error", e);
            event.getInteraction().getMember().get().getPrivateChannel().flatMap(
                    privateChannel -> privateChannel.createMessage("Что-то пошло не так... Попробуйте снова")
            ).block();
        }
    }
}
