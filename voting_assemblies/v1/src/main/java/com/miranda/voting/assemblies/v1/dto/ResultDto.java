package com.miranda.voting.assemblies.v1.dto;

import lombok.Data;

@Data
public class ResultDto {

    private String title;

    private Double result;
    private Integer totalVotes;

    private Integer upVote;

    private Integer falseVote;

    public ResultDto(String title, Double result, Integer totalVotes, Integer upVote, Integer falseVote) {
        this.title = title;
        this.result = result;
        this.totalVotes = totalVotes;
        this.upVote = upVote;
        this.falseVote = falseVote;
    }

    public ResultDto(){

    }
}
