package com.miranda.voting.assemblies.v1.dto;

import lombok.Data;

@Data
public class ResultDto {

    private String title;

    private Double result;
    private Integer totalVotes;

    private Integer upVote;

    private Integer falseVote;
}
