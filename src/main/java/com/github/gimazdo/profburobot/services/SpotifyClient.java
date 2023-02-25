package com.github.gimazdo.profburobot.services;

import se.michaelthelin.spotify.SpotifyApi;

public class SpotifyClient {


    private final SpotifyApi spotifyApi;

    public SpotifyClient(String refreshToken){
        spotifyApi = SpotifyApi.builder().setAccessToken(refreshToken).build();
    }
}
