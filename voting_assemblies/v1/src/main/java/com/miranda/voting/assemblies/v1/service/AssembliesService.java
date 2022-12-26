package com.miranda.voting.assemblies.v1.service;

import com.miranda.voting.assemblies.v1.dto.*;
import com.miranda.voting.assemblies.v1.entity.AssociateEntity;
import com.miranda.voting.assemblies.v1.entity.ScheduleEntity;

public interface AssembliesService {

    ScheduleEntity createSchedule(ScheduleDto request);

    VotingSlipDto vote(AssembliesDto assemblies);

    ScheduleEntity createAssemblies(AssembliesDto dto);

    ResultDto polling(Long assemblieId);

    AssociateEntity createAssociate(CreateAssociateDto req);
}
