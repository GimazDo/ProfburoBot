package com.github.gimazdo.profburobot.services;

import com.github.gimazdo.profburobot.discord.DiscordBot;
import com.vk.api.sdk.callback.CallbackApi;
import com.vk.api.sdk.objects.photos.Image;
import com.vk.api.sdk.objects.photos.ImageType;
import com.vk.api.sdk.objects.photos.PhotoSizes;
import com.vk.api.sdk.objects.photos.PhotoSizesType;
import com.vk.api.sdk.objects.wall.Wallpost;
import com.vk.api.sdk.objects.wall.WallpostAttachmentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CallbackApiHandler extends CallbackApi {
    private final DiscordBot discordBot;

    private final Long chatId = 1003740959831490632L;
    @Override
    public void wallPostNew(Integer groupId, Wallpost message) {
        try {
            log.info(message.getText());
            List<String> urls = message.getAttachments().stream()
                    .filter(p-> p.getType().equals(WallpostAttachmentType.PHOTO))
                    .map( p->
                    {
                        Optional<PhotoSizes> image = p.getPhoto().getSizes().stream().filter(s-> s.getType().equals(PhotoSizesType.W)).findFirst();
                        if(image.isPresent())
                            return image.get().getUrl().toString();
                        image = p.getPhoto().getSizes().stream().filter(s-> s.getType().equals(PhotoSizesType.R)).findFirst();
                        return image.map(s-> s.getUrl().toString()).orElse(null);
                    }).filter(Objects::nonNull).collect(Collectors.toList());
            discordBot.sendMessage(chatId, message.getText(), urls);
        }  catch (Exception e){
            log.error("Error {}", e.toString(), e);
        }

        super.wallPostNew(groupId, message);
    }
}
