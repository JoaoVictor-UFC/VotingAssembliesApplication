package com.miranda.voting.assemblies.v1.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScheduleDto {

    @NotNull
    private String title;

    private Integer totalVotes;

    private Double result;

    private String time;
}
