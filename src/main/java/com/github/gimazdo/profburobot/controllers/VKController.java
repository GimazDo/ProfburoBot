package com.github.gimazdo.profburobot.controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.github.gimazdo.profburobot.services.CallbackApiHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@Slf4j
public class VKController {

    private final CallbackApiHandler callbackApiHandler;
    @GetMapping
    public void get(@RequestBody String body){
        CompletableFuture.runAsync(()-> callbackApiHandler.parse(body));
    }
    @PostMapping
    public String post(@RequestBody JsonNode json){
        if(json.get("type").asText().equals("confirmation")){
            return  "b9f79615";
        }
        String body = json.toString();
        log.info("receive body {}", body);
        CompletableFuture.runAsync(()-> callbackApiHandler.parse(body));

        return "ok";
    }
}
