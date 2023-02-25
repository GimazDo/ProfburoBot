package com.github.gimazdo.profburobot.config;

import com.github.gimazdo.profburobot.discord.TrackScheduler;
import com.github.gimazdo.profburobot.services.LavaPlayerAudioProvider;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import discord4j.voice.AudioProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AudioPlayerConfig {

    @Bean
    public AudioPlayerManager playerManager(){
        final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        return playerManager;
    }

    @Bean
    public AudioProvider audioProvider(){
        return new LavaPlayerAudioProvider(audioPlayer());
    }

    @Bean
    public AudioPlayer audioPlayer(){
        return playerManager().createPlayer();
    }

    @Bean
    public TrackScheduler trackScheduler(){
        return new TrackScheduler(audioPlayer());
    }
}
