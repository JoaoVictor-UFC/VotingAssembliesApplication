package com.miranda.voting.assemblies.v1.service;

import com.miranda.voting.assemblies.v1.dto.*;
import com.miranda.voting.assemblies.v1.entity.AssembliesEntity;
import com.miranda.voting.assemblies.v1.entity.AssociateEntity;
import com.miranda.voting.assemblies.v1.entity.ScheduleEntity;
import com.miranda.voting.assemblies.v1.enums.ValidateVoteStatus;
import com.miranda.voting.assemblies.v1.errorExceptions.ResourceBadRequestException;
import com.miranda.voting.assemblies.v1.errorExceptions.ResourceForbiddenException;
import com.miranda.voting.assemblies.v1.errorExceptions.ResourceNotFoundException;
import com.miranda.voting.assemblies.v1.repository.AssembliesRepository;
import com.miranda.voting.assemblies.v1.repository.AssociateRepository;
import com.miranda.voting.assemblies.v1.repository.ScheduleRepository;
import com.miranda.voting.assemblies.v1.util.ValidateVote;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.miranda.voting.assemblies.v1.enums.ValidateVoteStatus.UNABLE_TO_VOTE;

@Service
public class AssembliesServiceImp implements AssembliesService {

    private final AssembliesRepository assembliesRepository;
    private final AssociateRepository associateRepository;
    private final ScheduleRepository scheduleRepository;
    private final ValidateVote validateVote;

    public AssembliesServiceImp(AssembliesRepository assembliesRepository, AssociateRepository associateRepository, ScheduleRepository scheduleRepository, ValidateVote validateVote) {
        this.assembliesRepository = assembliesRepository;
        this.associateRepository = associateRepository;
        this.scheduleRepository = scheduleRepository;
        this.validateVote = validateVote;
    }

    @Transactional
    public ScheduleEntity createSchedule(CreateScheduleDto request){
        ScheduleEntity entity = new ScheduleEntity();
        entity.setTitle(request.getTitle());
        entity.setCreatedAt(LocalDateTime.now());
        if (request.getTime() != null) entity.setTime(LocalDateTime.parse(request.getTime()));
        return scheduleRepository.save(entity);
    }

    @Transactional
    public VotingSlipDto votingSlip(VoteDto voting){
        Optional<ScheduleEntity> schedule = scheduleRepository.findById(voting.getScheduleId());
        Optional<AssociateEntity> associate = associateRepository.findByCpf(voting.getCpfAssociate());
        Optional<AssembliesEntity> assemblies = assembliesRepository.existsByAssociateId(associate.get().getId());
        if (schedule.isEmpty()) throw new ResourceBadRequestException("Schedule not found");
        if (associate.isEmpty()) throw new ResourceBadRequestException("Associate not found");
        if (!assemblies.isEmpty())throw new ResourceBadRequestException("associate has already voted");
        if (schedule.get().getTime().isBefore(LocalDateTime.now())) throw new ResourceForbiddenException("Stalled voting time");
        AssembliesEntity entity = new AssembliesEntity();
        entity.setVote(voting.getVote());
        entity.setSchedule(schedule.get());
        entity.setAssociate(associate.get());
        entity.setCreatedAt(LocalDateTime.now());
        assembliesRepository.save(entity);
        VotingSlipDto dto = new VotingSlipDto();
        dto.setVote(voting.getVote());
        dto.setDateVoting(LocalDateTime.now());
        dto.setNameAssociate(associate.get().getName());
        dto.setTitleSchedule(schedule.get().getTitle());
        return dto;
    }

    @Transactional
    public ResultDto polling(Long scheduleId){
        List<AssembliesEntity> assembliesEntity = assembliesRepository.findAllByScheduleId(scheduleId);
        Optional<ScheduleEntity> scheduleEntity = scheduleRepository.findById(scheduleId);

        if (assembliesEntity.isEmpty() || scheduleEntity.isEmpty()){
            throw new ResourceNotFoundException("Schedule or Assemblies were not found ");
        }
        Double totalVotes = (double) assembliesEntity.size();
        Double upvote = (double) assembliesEntity.stream().filter(voteEntity -> voteEntity.getVote().equals(true)).count();
        Double result = upvote/totalVotes;
        scheduleEntity.get().setResult(result);
        scheduleEntity.get().setTotalVotes(totalVotes.intValue());
        scheduleRepository.save(scheduleEntity.get());
        ResultDto dto = new ResultDto();
        DecimalFormat fmt = new DecimalFormat("0.00");
        dto.setResult(Double.valueOf(fmt.format(result)));
        dto.setTotalVotes(totalVotes.intValue());
        dto.setTitle(scheduleEntity.get().getTitle());
        dto.setUpVote(upvote.intValue());
        dto.setFalseVote((int) assembliesEntity.stream().filter(voteEntity -> voteEntity.getVote().equals(false)).count());
        return dto;
    }

    @Transactional
    public AssociateEntity createAssociate(CreateAssociateDto req){
        AssociateEntity entity = new AssociateEntity();
        if (associateRepository.findByCpf(req.getCpf()).isPresent()){
            throw new ResourceBadRequestException("cpf already registered");
        }
        if (!Objects.equals(validateVote.validateVote(req.getCpf()).getDescription(), UNABLE_TO_VOTE.getDescription())) {
            entity.setCpf(req.getCpf());
            entity.setName(req.getName());
            entity.setCreatedAt(LocalDateTime.now());
            return associateRepository.save(entity);
        }
        throw new ResourceBadRequestException(UNABLE_TO_VOTE.getDescription());
    }
}
