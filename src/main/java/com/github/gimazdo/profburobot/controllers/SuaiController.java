package com.github.gimazdo.profburobot.controllers;

import com.github.gimazdo.profburobot.dto.SUAIUSer;
import com.github.gimazdo.profburobot.services.SUAIClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("suai/")
@RequiredArgsConstructor
public class SuaiController {
    private final SUAIClient suaiClient;
    @PostMapping("login")
    public SUAIUSer suaiuSer(@RequestParam String username,@RequestParam String password){
       return suaiClient.getUserInfo(username, password);
    }
}
