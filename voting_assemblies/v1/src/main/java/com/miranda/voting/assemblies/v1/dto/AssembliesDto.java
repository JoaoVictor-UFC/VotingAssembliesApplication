package com.miranda.voting.assemblies.v1.dto;

import lombok.Data;

@Data
public class AssembliesDto {

    private Long associateId;

    private Boolean vote;

    private String time;

    private Long scheduleId;

    private Long assemblieId;
}
