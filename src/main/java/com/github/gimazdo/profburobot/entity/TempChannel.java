package com.github.gimazdo.profburobot.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@ToString
public class TempChannel {

    @Id
    private Integer id;

    private LocalDateTime expiredDate;

    @Column("discord_id")
    private Long discordId;
}
