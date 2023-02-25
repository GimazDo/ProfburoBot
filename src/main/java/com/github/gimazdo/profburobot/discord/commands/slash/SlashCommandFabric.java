package com.github.gimazdo.profburobot.discord.commands.slash;

import com.github.gimazdo.profburobot.services.TempChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SlashCommandFabric {
    @Value("${discord.registred-member-role-id:1008276568252096543}")
    private Long registeredMemberRoleId;

    private final TempChannelService tempChannelService;

    public List<SlashCommand> getAll(){
        List<SlashCommand> commands = new ArrayList<>();
        commands.add(new RegisterCommand());
        commands.add(new CreateTempChannel(tempChannelService));
        commands.add(new RandomKickCommand());
        return commands;
    }
}
