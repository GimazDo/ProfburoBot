package com.github.gimazdo.profburobot.discord.commands.text;

import com.github.gimazdo.profburobot.discord.commands.messages.Messages;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.discordjson.json.MessageCreateRequest;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;

public class GreetingCommand implements Command{


    private final Long newMemberRoleId;

    public GreetingCommand(Long newMemberRoleId) {
        this.newMemberRoleId = newMemberRoleId;
    }

    @Override
    public Mono<Void> command(MessageCreateEvent event) {
        Message message = event.getMessage();
        if(message.getType().equals(Message.Type.GUILD_MEMBER_JOIN))
            return event.getMember().get()
                    .getPrivateChannel()
                    .flatMap(channel -> channel.createMessage(messageCreateSpec ->
                    {
                        messageCreateSpec.setContent(Messages.GREETING_MESSAGE);
                        messageCreateSpec.addFile("output.png",TestMessage.class.getClassLoader().getResourceAsStream("findSetting.png") );
                        messageCreateSpec.addFile("output2.png",TestMessage.class.getClassLoader().getResourceAsStream("profileSettings.png") );
                        messageCreateSpec.addFile("output.png",TestMessage.class.getClassLoader().getResourceAsStream("setServer.png") );
                        messageCreateSpec.addFile("output1.png",TestMessage.class.getClassLoader().getResourceAsStream("changeName.png") );
                    }))
                    .and(event.getMember()
                            .get()
                            .addRole(Snowflake.of(newMemberRoleId)));
        return Mono.empty();
    }
}
