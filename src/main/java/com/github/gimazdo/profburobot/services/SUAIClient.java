package com.github.gimazdo.profburobot.services;

import com.github.gimazdo.profburobot.dto.SUAIUSer;
import com.github.gimazdo.profburobot.dto.UriAndCookie;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SUAIClient {


    //TODO FIX THIS SHIT
    private static final String INSIDE = "/inside/profile";
    private final WebClient proGuapClient;
    private final WebClient authClient;
    public SUAIClient(@Value("${app.suai.url}") String url) {
        authClient = WebClient.builder().baseUrl(url).build();
        proGuapClient = WebClient.builder().baseUrl(url).build();
    }


        @SneakyThrows
    public SUAIUSer getUserInfo(String username, String password) {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("username", username);
            formData.add("password", password);
            UriAndCookie uriAndCookie = getAuthUrl();

          var response = authClient.post()
                 .uri(uriAndCookie.getUri())
                 .contentType(MediaType.MULTIPART_FORM_DATA)
                 .body(BodyInserters.fromFormData(formData))
                  .header(HttpHeaders.REFERER, "https://pro.guap.ru/")
                  .header(HttpHeaders.COOKIE, String.join(";", uriAndCookie.getCookie()))
                 .retrieve()
                 .toEntity(String.class)
                  .block();
        var response2 = authClient.get()
                .uri(response.getHeaders().getLocation())
                .retrieve()
                .toEntity(String.class)
                .block();
         var  cookie = response2.getHeaders()
                    .get(HttpHeaders.SET_COOKIE);
         Document document = Jsoup.parse(proGuapClient.get().uri(INSIDE).header(HttpHeaders.COOKIE, cookie.stream().collect(Collectors.joining(";"))).retrieve().toEntity(String.class).block().getBody(), "UTF8");
            Element element = document.select("div").stream().filter(p-> p.className().equals("card-body")).findFirst().get();
            element.getElementsByTag("h3").get(0).childNode(0);
            String[] s = element.getElementsByTag("h3").get(0).childNode(0).toString().split(" ");
            SUAIUSer suaiuSer = new SUAIUSer(
            );

            suaiuSer.setLastname(s[0]);
            suaiuSer.setFirstname(s[1]);
            suaiuSer.setMiddlename(s.length==3?s[2]:null);
            return suaiuSer;
    }

    public UriAndCookie getAuthUrl(){
        UriAndCookie uriAndCookie = getAuthFormUrl();
        ResponseEntity<String> stringResponseEntity = authClient
                .get()
                .uri(uriAndCookie.getUri())
                .retrieve()
                .toEntity(String.class)
                .block();
        Document document = Jsoup.parse(stringResponseEntity.getBody());
        return new UriAndCookie(URI.create(document.select("form").get(0).attributes().get("action")), stringResponseEntity.getHeaders().get(HttpHeaders.SET_COOKIE));
    }

    public UriAndCookie getAuthFormUrl(){
        HttpHeaders httpHeaders = proGuapClient
                .get()
                .uri("/oauth/login")
                .retrieve()
                .toEntity(String.class)
                .block()
                .getHeaders();
        return new UriAndCookie(httpHeaders.getLocation(), httpHeaders.get(HttpHeaders.SET_COOKIE));
    }


}
