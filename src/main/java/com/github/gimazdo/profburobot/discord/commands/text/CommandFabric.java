package com.github.gimazdo.profburobot.discord.commands.text;

import com.github.gimazdo.profburobot.discord.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import discord4j.voice.AudioProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommandFabric {
    @Value("${discord.registred-member-role-id:1008276568252096543}")
    private Long registredMemberRoleId;

    @Value("${discord.new-member-role-id:1008276568252096543}")
    private Long newMemberRoleId;
    private final AudioProvider audioProvider;
    private final AudioPlayer audioPlayer;
    private final AudioPlayerManager audioPlayerManager;

    private final TrackScheduler trackScheduler;
    public List<Command> getAllCommands(){
        List<Command> commands = new ArrayList<>();

        commands.add(new GreetingCommand(newMemberRoleId));
        commands.add(new PingCommand());
        commands.add(new MusicJoinCommand(audioProvider));
        commands.add(new MusicPlayCommand(audioPlayerManager, trackScheduler));
        return commands;
    }

}
