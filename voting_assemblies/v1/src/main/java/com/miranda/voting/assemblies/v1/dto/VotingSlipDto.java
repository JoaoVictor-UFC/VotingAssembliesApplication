package com.miranda.voting.assemblies.v1.dto;

import com.miranda.voting.assemblies.v1.repository.AssembliesRepository;
import com.miranda.voting.assemblies.v1.repository.AssociateRepository;
import com.miranda.voting.assemblies.v1.repository.ScheduleRepository;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VotingSlipDto {

    private String nameAssociate;

    private LocalDateTime dateVoting;

    private Boolean vote;

    private String titleSchedule;

    public VotingSlipDto() {

    }

    public VotingSlipDto(ScheduleRepository scheduleRepository, AssociateRepository associateRepository, AssembliesRepository assembliesRepository) {
    }
}
