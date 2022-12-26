package com.miranda.voting.assemblies.v1.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AssembliesDto {

    private Long associateId;

    private Boolean vote;

    private LocalDateTime time;

    private Long scheduleId;

    private Long assemblieId;
}
