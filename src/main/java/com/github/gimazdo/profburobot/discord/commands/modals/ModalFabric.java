package com.github.gimazdo.profburobot.discord.commands.modals;

import com.github.gimazdo.profburobot.services.SUAIClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ModalFabric {

    @Value("${discord.registred-member-role-id:1008276568252096543}")
    private Long registredMemberRoleId;

    @Value("${discord.new-member-role-id:1008276568252096543}")
    private  Long newMemberRoleId;

    private final SUAIClient suaiClient;


    public List<ModalResolver> getAll(){
        List<ModalResolver> modalResolvers = new ArrayList<>();
        modalResolvers.add(new RegisterModal(registredMemberRoleId, newMemberRoleId, suaiClient));
        return modalResolvers;
    }
}
