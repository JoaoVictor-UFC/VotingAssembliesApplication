package com.miranda.voting.assemblies.v1.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VotingSlipDto {

    private String nameAssociate;

    private LocalDateTime dateVote;
}
