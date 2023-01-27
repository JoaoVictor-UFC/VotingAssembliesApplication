package com.miranda.voting.assemblies.v1.service;

import com.miranda.voting.assemblies.v1.dto.*;
import com.miranda.voting.assemblies.v1.entity.AssembliesEntity;
import com.miranda.voting.assemblies.v1.entity.AssociateEntity;
import com.miranda.voting.assemblies.v1.entity.ScheduleEntity;

public interface AssembliesService {

    ScheduleEntity createSchedule(CreateScheduleDto request);

    VotingSlipDto votingSlip(VoteDto voting);

    ResultDto polling(Long scheduleId);

    AssociateEntity createAssociate(CreateAssociateDto req);
}
