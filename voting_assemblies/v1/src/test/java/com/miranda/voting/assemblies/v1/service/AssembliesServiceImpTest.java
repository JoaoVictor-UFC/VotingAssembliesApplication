package com.miranda.voting.assemblies.v1.service;

import com.miranda.voting.assemblies.v1.dto.CreateAssociateDto;
import com.miranda.voting.assemblies.v1.dto.ResultDto;
import com.miranda.voting.assemblies.v1.dto.VoteDto;
import com.miranda.voting.assemblies.v1.dto.VotingSlipDto;
import com.miranda.voting.assemblies.v1.entity.AssembliesEntity;
import com.miranda.voting.assemblies.v1.entity.AssociateEntity;
import com.miranda.voting.assemblies.v1.entity.ScheduleEntity;
import com.miranda.voting.assemblies.v1.errorExceptions.ResourceBadRequestException;
import com.miranda.voting.assemblies.v1.errorExceptions.ResourceNotFoundException;
import com.miranda.voting.assemblies.v1.repository.AssembliesRepository;
import com.miranda.voting.assemblies.v1.repository.AssociateRepository;
import com.miranda.voting.assemblies.v1.repository.ScheduleRepository;
import com.miranda.voting.assemblies.v1.util.ValidateVote;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.miranda.voting.assemblies.v1.enums.ValidateVoteStatus.UNABLE_TO_VOTE;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssembliesServiceImpTest {
    @Mock
    private ValidateVote validateVote;
    @Mock
    private EntityManager em;
    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private AssociateRepository associateRepository;
    @Mock
    private AssembliesRepository assembliesRepository;
    @InjectMocks
    private AssembliesServiceImp serviceImp;

    private static final String CPF = "12345678900";
    private static final String name = "Test Test";

    @Test
    void whenSchedulePersistDatesInDatabase() {
        lenient().doAnswer((InvocationOnMock invocation) -> {
            ScheduleEntity schedule = (ScheduleEntity) invocation.getArguments()[0];
            schedule.setId(1L);
            schedule.setTitle(name);
            schedule.setResult(Mockito.anyDouble());
            schedule.setTime(Mockito.any());
            schedule.setTotalVotes(Mockito.any());
            return null;
        }).when(em).persist(any(AssociateEntity.class));
    }
    @Test
    public void testVotingSlip_Success() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setTime(LocalDateTime.now().plusDays(1));
        lenient().when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        AssociateEntity associateEntity = new AssociateEntity();
        associateEntity.setCpf(CPF);
        associateEntity.setName(name);
        associateEntity.setId(1L);
        lenient().when(associateRepository.findByCpf(CPF)).thenReturn(Optional.of(associateEntity));
        lenient().when(assembliesRepository.existsByAssociateId(1L)).thenReturn(false);

        VoteDto voting = new VoteDto();
        voting.setCpfAssociate(CPF);
        voting.setScheduleId(1L);
        voting.setVote(true);

        VotingSlipDto votingSlip = new VotingSlipDto();
        votingSlip.setVote(true);
        votingSlip.setNameAssociate(name);
        votingSlip.setTitleSchedule(name);

        assertEquals(true, votingSlip.getVote());
        assertEquals(name, votingSlip.getNameAssociate());
        assertEquals(name, votingSlip.getTitleSchedule());
    }

    @Test
    public void testVotingSlip_ScheduleNotFound() {
        lenient().when(scheduleRepository.findById(1L)).thenReturn(Optional.empty());

        VoteDto voting = new VoteDto();
        voting.setCpfAssociate(CPF);
        voting.setScheduleId(1L);
        voting.setVote(true);

        assertThrows(ResourceBadRequestException.class, () -> serviceImp.votingSlip(voting));
    }

    @Test
    public void testVotingSlip_whenAssociateNotFound_shouldThrowResourceBadRequestException() {
        VoteDto voting = new VoteDto();
        voting.setScheduleId(1L);
        voting.setCpfAssociate(CPF);
        voting.setVote(true);

        lenient().when(scheduleRepository.findById(1L)).thenReturn(Optional.of(new ScheduleEntity()));
        lenient().when(associateRepository.findByCpf(CPF)).thenReturn(Optional.empty());

        assertThrows(ResourceBadRequestException.class, () -> serviceImp.votingSlip(voting));
    }

    @Test
    public void testVotingSlip_whenScheduleNotFound_shouldThrowResourceBadRequestException() {
        VoteDto voting = new VoteDto();
        voting.setScheduleId(1L);
        voting.setCpfAssociate(CPF);
        voting.setVote(true);

        lenient().when(scheduleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceBadRequestException.class, () -> serviceImp.votingSlip(voting));
    }

    @Test
    public void testVotingSlip_whenAssociateAlreadyVoted_shouldThrowResourceBadRequestException() {
        VoteDto voting = new VoteDto();
        voting.setScheduleId(1L);
        voting.setCpfAssociate(CPF);
        voting.setVote(true);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        AssociateEntity associateEntity = new AssociateEntity();
        associateEntity.setId(1L);
        associateEntity.setCpf(CPF);

        lenient().when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        lenient().when(associateRepository.findByCpf(CPF)).thenReturn(Optional.of(associateEntity));
        lenient().when(assembliesRepository.existsByAssociateId(1L)).thenReturn(true);

        assertThrows(ResourceBadRequestException.class, () -> serviceImp.votingSlip(voting));
    }

    @Test
    public void testVotingSlip_whenStalledVotingTime_shouldThrowResourceForbiddenException() {
        VoteDto voteDto = new VoteDto();
        voteDto.setCpfAssociate(CPF);
        voteDto.setScheduleId(1L);
        voteDto.setVote(true);

        ScheduleEntity entity = new ScheduleEntity();
        entity.setId(1L);
        entity.setTitle(name);
        entity.setTime(LocalDateTime.of(2023, 12, 12, 12, 12));

        AssociateEntity associateEntity = new AssociateEntity();
        associateEntity.setId(1L);
        associateEntity.setCpf(CPF);
        associateEntity.setName(name);

        when(scheduleRepository.findById(voteDto.getScheduleId())).thenReturn(Optional.of(entity));
        when(associateRepository.findByCpf(voteDto.getCpfAssociate())).thenReturn(Optional.of(associateEntity));
        when(assembliesRepository.existsByAssociateId(associateEntity.getId())).thenReturn(false);

        lenient().when(serviceImp.votingSlip(voteDto)).thenReturn(new VotingSlipDto());
    }

    @Test
    public void testPolling_whenValidParams_shouldReturnResultDto() {
        Long scheduleId = 1L;
        ScheduleEntity schedule = new ScheduleEntity();
        schedule.setId(scheduleId);
        schedule.setTitle(name);
        scheduleRepository.save(schedule);

        AssociateEntity associate = new AssociateEntity();
        associate.setName(name);
        associate.setCpf(CPF);
        associateRepository.save(associate);

        AssembliesEntity assembly1 = new AssembliesEntity();
        assembly1.setVote(true);
        assembly1.setAssociate(associate);
        assembly1.setSchedule(schedule);
        assembliesRepository.save(assembly1);

        AssembliesEntity assembly2 = new AssembliesEntity();
        assembly2.setVote(false);
        assembly2.setAssociate(associate);
        assembly2.setSchedule(schedule);
        assembliesRepository.save(assembly2);

        ResultDto result = new ResultDto(name, 0.50, 2, 1, 1);
        assertNotNull(result);
        assertEquals("0.5", String.valueOf(result.getResult()));
        assertEquals(2, result.getTotalVotes());
        assertEquals(name, result.getTitle());
        assertEquals(1, result.getUpVote());
        assertEquals(1, result.getFalseVote());
    }

    @Test
    void testPolling_whenScheduleOrAssembliesNotFound_shouldThrowResourceNotFoundException() {
        Long scheduleId = 1L;
        lenient().when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());
        lenient().when(assembliesRepository.findAllByScheduleId(scheduleId)).thenReturn(Collections.emptyList());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> serviceImp.polling(scheduleId));
        assertEquals("Schedule or Assemblies were not found ", exception.getMessage());
    }

    @Test
    void testPolling_whenValidPolling_shouldReturnResultDto() {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        scheduleEntity.setTitle(name);
        lenient().when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));
        List<AssembliesEntity> assembliesEntity = new ArrayList<>();
        assembliesEntity.add(new AssembliesEntity(true, scheduleEntity));
        assembliesEntity.add(new AssembliesEntity(false, scheduleEntity));
        lenient().when(assembliesRepository.findAllByScheduleId(scheduleId)).thenReturn(assembliesEntity);
        ResultDto result = new ResultDto();
        result.setFalseVote(1);
        result.setResult(1.0);
        result.setUpVote(1);
        result.setTitle(name);
        result.setTotalVotes(2);
        assertEquals(1.0, result.getResult(), 0.001);
        assertEquals(2, result.getTotalVotes());
        assertEquals(name, result.getTitle());
        assertEquals(1, result.getUpVote());
        assertEquals(1, result.getFalseVote());
        verify(scheduleRepository, times(0)).findById(scheduleId);
        verify(assembliesRepository, times(0)).findAllByScheduleId(scheduleId);
        verify(scheduleRepository, times(0)).save(scheduleEntity);
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
        CreateAssociateDto associateDto = new CreateAssociateDto();
        associateDto.setCpf(CPF);
        associateDto.setName(name);
        Mockito.when(associateRepository.findByCpf(CPF)).thenReturn(Optional.empty());
        Mockito.when(validateVote.validateVote(CPF)).thenReturn(UNABLE_TO_VOTE);
        assertThrows(ResourceBadRequestException.class, () -> {
            serviceImp.createAssociate(associateDto);
        });
    }

    @Test
    void whenAssociatePersistDatesInDatabase() {
        lenient().doAnswer((InvocationOnMock invocation) -> {
            AssociateEntity associate = (AssociateEntity) invocation.getArguments()[0];
            associate.setId(1L);
            associate.setName(name);
            associate.setCpf(CPF);
            return null;
        }).when(em).persist(any(AssociateEntity.class));
    }
}