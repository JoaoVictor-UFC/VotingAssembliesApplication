package com.miranda.voting.assemblies.v1.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleDto {

    private String title;

    private Integer totalVotes;

    private Double result;

    private LocalDateTime time;
}
