package com.github.gimazdo.profburobot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@Data
public class UriAndCookie {
    private URI uri;
    private List<String> cookie;
}
