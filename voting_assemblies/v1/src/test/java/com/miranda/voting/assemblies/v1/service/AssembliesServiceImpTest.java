package com.miranda.voting.assemblies.v1.service;

import com.miranda.voting.assemblies.v1.dto.CreateAssociateDto;
import com.miranda.voting.assemblies.v1.dto.CreateScheduleDto;
import com.miranda.voting.assemblies.v1.entity.AssociateEntity;
import com.miranda.voting.assemblies.v1.entity.ScheduleEntity;
import com.miranda.voting.assemblies.v1.errorExceptions.ResourceBadRequestException;
import com.miranda.voting.assemblies.v1.errorExceptions.ResourceUnauthorizedException;
import com.miranda.voting.assemblies.v1.repository.AssembliesRepository;
import com.miranda.voting.assemblies.v1.repository.AssociateRepository;
import com.miranda.voting.assemblies.v1.repository.ScheduleRepository;
import com.miranda.voting.assemblies.v1.util.ValidateVote;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static com.miranda.voting.assemblies.v1.enums.ValidateVoteStatus.UNABLE_TO_VOTE;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AssembliesServiceImpTest {

    @Mock
    private AssembliesRepository assembliesRepository;
    @Mock
    private AssociateRepository associateRepository;
    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private ValidateVote validateVote;

    private static final String CPF = "02282660099";

    @InjectMocks
    private AssembliesServiceImp serviceImp;

    @Test
    void createSchedule() {
        ScheduleEntity schedule = new ScheduleEntity();
        CreateScheduleDto request = new CreateScheduleDto();
        request.setTitle("Pokemon");
        Mockito.when(scheduleRepository.save(schedule));
    }

    @Test
    void votingSlip() {
    }

    @Test
    void polling() {
    }

    @Test
    void whenAssociateIsPresentInDataBaseThrowResourceBadRequestException() {
        CreateAssociateDto req = new CreateAssociateDto();
        req.setCpf(CPF);
        Mockito.when(associateRepository.findByCpf(req.getCpf()))
                .thenReturn(Optional.of(new AssociateEntity()));
        assertThrows(ResourceBadRequestException.class, () -> {
            serviceImp.createAssociate(req);
        });
    }

    @Test
    void whenAssociateCpfReturnUnableToVoteThrowResourceBadRequestException() {
        CreateAssociateDto req = new CreateAssociateDto();
        req.setCpf(CPF);
        Mockito.when(associateRepository.findByCpf(CPF)).thenReturn(Optional.empty());
        Mockito.when(validateVote.validateVote(CPF)).thenReturn(UNABLE_TO_VOTE);
        assertThrows(ResourceBadRequestException.class, () -> {
            serviceImp.createAssociate(req);
        });
    }
}