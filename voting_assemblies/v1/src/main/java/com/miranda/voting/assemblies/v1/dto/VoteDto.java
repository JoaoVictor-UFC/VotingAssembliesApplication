package com.miranda.voting.assemblies.v1.dto;

import lombok.Data;

@Data
public class VoteDto {

    private String cpfAssociate;
    private Boolean vote;
    private Long scheduleId;
}
