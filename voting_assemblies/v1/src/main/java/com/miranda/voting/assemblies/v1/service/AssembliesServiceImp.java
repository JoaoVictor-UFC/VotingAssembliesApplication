package com.miranda.voting.assemblies.v1.service;

import com.miranda.voting.assemblies.v1.dto.*;
import com.miranda.voting.assemblies.v1.entity.AssembliesEntity;
import com.miranda.voting.assemblies.v1.entity.AssociateEntity;
import com.miranda.voting.assemblies.v1.entity.ScheduleEntity;
import com.miranda.voting.assemblies.v1.entity.VoteEntity;
import com.miranda.voting.assemblies.v1.errorExceptions.ResourceBadRequestException;
import com.miranda.voting.assemblies.v1.errorExceptions.ResourceNotFoundException;
import com.miranda.voting.assemblies.v1.repository.AssembliesRepository;
import com.miranda.voting.assemblies.v1.repository.AssociateRepository;
import com.miranda.voting.assemblies.v1.repository.ScheduleRepository;
import com.miranda.voting.assemblies.v1.repository.VoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AssembliesServiceImp implements AssembliesService {

    private final AssembliesRepository assembliesRepository;
    private final AssociateRepository associateRepository;
    private final ScheduleRepository scheduleRepository;
    private final VoteRepository voteRepository;

    public AssembliesServiceImp(AssembliesRepository assembliesRepository, AssociateRepository associateRepository, ScheduleRepository scheduleRepository, VoteRepository voteRepository) {
        this.assembliesRepository = assembliesRepository;
        this.associateRepository = associateRepository;
        this.scheduleRepository = scheduleRepository;
        this.voteRepository = voteRepository;
    }

    @Transactional
    public ScheduleEntity createSchedule(ScheduleDto request){
        ScheduleEntity entity = new ScheduleEntity();
        List<AssembliesEntity> assemblies = new ArrayList<>();
        entity.setTitle(request.getTitle());
        AssembliesEntity a = new AssembliesEntity();
        if (request.getTime() == null){
            a.setTime(LocalDateTime.now().plusMinutes(1));
        }else {
            a.setTime(convertStringToDate(request.getTime()));
        }
        a.setCreatedAt(LocalDateTime.now());
        assemblies.add(a);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setAssembliesEntities(assemblies);

        return scheduleRepository.save(entity);
    }

    @Transactional
    public VotingSlipDto vote(AssembliesDto assemblies){
        Optional<AssembliesEntity> assembliesEntity = assembliesRepository.findById(assemblies.getAssemblieId());
        Optional<ScheduleEntity> scheduleEntity = scheduleRepository.findById(assemblies.getScheduleId());
        Optional<AssociateEntity> associateEntity = associateRepository.findById(assemblies.getAssociateId());
        if (assembliesEntity.isEmpty()){
            createAssemblies(assemblies);
            scheduleEntity.get().setAssembliesEntities(assembliesRepository.findAll());
        }

        List<VoteEntity> listVotes = voteRepository.findAll();
        for (VoteEntity v : listVotes) {
            if (Objects.equals(v.getAssociate().getId(), assemblies.getAssociateId())){
                throw new ResourceBadRequestException("associate has already voted");
            }
        }

        List<VoteEntity> votes =  new ArrayList<>(voteRepository.findAllByAssemblieId(assemblies.getAssemblieId()));
        VoteEntity voteEntity = new VoteEntity();
        voteEntity.setVote(assemblies.getVote());
        voteEntity.setAssociate(associateEntity.get());
        voteEntity.setCreatedAt(LocalDateTime.now());
        votes.add(voteEntity);

        assembliesEntity.get().setVotes(votes);
        assembliesRepository.save(assembliesEntity.get());

        VotingSlipDto response = new VotingSlipDto();
        response.setDateVote(LocalDateTime.now());
        response.setNameAssociate(associateEntity.get().getName());
        return response;
    }

    @Transactional
    public ScheduleEntity createAssemblies(AssembliesDto dto){
        Optional<ScheduleEntity> schedule = scheduleRepository.findById(dto.getScheduleId());
        if (schedule.isEmpty()){
            throw new ResourceNotFoundException("Schedule is not exist");
        }
        List<AssembliesEntity> assembliesEntity = new ArrayList<>(assembliesRepository.findAllByScheduleId(dto.getScheduleId()));
        AssembliesEntity entity = new AssembliesEntity();
        entity.setTime(convertStringToDate(dto.getTime()));
        entity.setCreatedAt(LocalDateTime.now());
        assembliesEntity.add(entity);
        schedule.get().setAssembliesEntities(assembliesEntity);
        return scheduleRepository.save(schedule.get());
    }

    @Transactional
    public ResultDto polling(Long assemblieId){
        Optional<AssembliesEntity> assembliesEntity = assembliesRepository.findById(assemblieId);
        Optional<ScheduleEntity> scheduleEntity = scheduleRepository.findById(assembliesRepository.findByScheduleId(assemblieId));

        if (assembliesEntity.isEmpty() || scheduleEntity.isEmpty()){
            throw new ResourceNotFoundException("Schedule or Assemblies were not found ");
        }
        Double totalVotes = (double) assembliesEntity.get().getVotes().size();
        Double upvote = (double) assembliesEntity.get().getVotes().stream().filter(voteEntity -> voteEntity.getVote().equals(true)).count();
        Double result = totalVotes/upvote;
        scheduleEntity.get().setResult(result);
        scheduleEntity.get().setTotalVotes(totalVotes.intValue());
        scheduleRepository.save(scheduleEntity.get());
        ResultDto dto = new ResultDto();
        DecimalFormat fmt = new DecimalFormat("0.00");
        dto.setResult(Double.valueOf(fmt.format(result)));
        dto.setTotalVotes(totalVotes.intValue());
        return dto;
    }

    @Transactional
    public AssociateEntity createAssociate(CreateAssociateDto req){
        AssociateEntity entity = new AssociateEntity();
        if (associateRepository.findByCpf(req.getCpf()).isPresent()){
            throw new ResourceBadRequestException("cpf already registered");
        }
        entity.setCpf(req.getCpf());
        entity.setName(req.getName());
        entity.setCreatedAt(LocalDateTime.now());
        return associateRepository.save(entity);
    }

    private LocalDateTime convertStringToDate(String date){
        DateTimeFormatter parser = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd HH:mm:ss")
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0).toFormatter();
        return LocalDateTime.parse(date, parser);
    }
}
