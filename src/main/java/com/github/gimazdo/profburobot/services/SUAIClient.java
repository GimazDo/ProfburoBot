package com.github.gimazdo.profburobot.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gimazdo.profburobot.dto.SUAIUSer;
import com.github.gimazdo.profburobot.dto.UserInfo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SUAIClient {

    private static final String LOGIN = "/user/login_check";
    private static final String MAIN = "/exters/";
    private static final String LOGIN_REDIRECT = "/login_redirect";
    private static final String INSIDE = "/inside_s";
    private static final String GET_INFO = "/getstudentprofile/";

    private final RestTemplate restTemplate;

    public SUAIClient(@Value("${app.suai.url}") String url) {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        this.restTemplate = restTemplateBuilder.rootUri(url).build();
    }


        @SneakyThrows
    public SUAIUSer getUserInfo(String username, String password) {

        HttpEntity<?> requestMain = new HttpEntity<>(null);

        ResponseEntity<?> responseMain = restTemplate.exchange(MAIN, HttpMethod.GET, requestMain, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.addAll(HttpHeaders.COOKIE, Objects.requireNonNullElse(responseMain.getHeaders().get(HttpHeaders.SET_COOKIE), new ArrayList<>()));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("_username", username);
        map.add("_password", password);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(LOGIN, request, String.class);
        log.info("{}", response);
        List<String> cookie = Objects.requireNonNullElse(response.getHeaders().get(HttpHeaders.SET_COOKIE),
                new ArrayList<String>())
                .stream()
                .filter(p-> p.contains("PHP")).collect(Collectors.toList());
        if (cookie.isEmpty()) {
            return null;
        }

        HttpHeaders headers2 = new HttpHeaders();
        headers2.addAll(HttpHeaders.COOKIE, cookie);
        HttpEntity<?> request2 = new HttpEntity<>(headers2);

        ResponseEntity<String> response2 = restTemplate.exchange(LOGIN_REDIRECT, HttpMethod.GET, request2, String.class);
        log.info("{}", response2);


        String body = response2.getBody();
        int s = body.indexOf("window.__initialServerData =") + 29;
        int s2 = body.indexOf(";", s);
        body = body.substring(s, s2);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(body);
        jsonNode = jsonNode.get("user");
        jsonNode = jsonNode.get(0);
        SUAIUSer suaiuSer = new SUAIUSer();
        suaiuSer.setFirstname(jsonNode.get("firstname").asText());
        suaiuSer.setMiddlename(jsonNode.get("middlename").asText());
        suaiuSer.setLastname(jsonNode.get("lastname").asText());
        return suaiuSer;
    }

}
