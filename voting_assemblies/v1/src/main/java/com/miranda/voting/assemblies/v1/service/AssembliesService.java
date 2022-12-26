package com.miranda.voting.assemblies.v1.service;

import com.miranda.voting.assemblies.v1.dto.*;
import com.miranda.voting.assemblies.v1.entity.AssembliesEntity;
import com.miranda.voting.assemblies.v1.entity.AssociateEntity;
import com.miranda.voting.assemblies.v1.entity.ScheduleEntity;
import com.miranda.voting.assemblies.v1.errorExceptions.ResourceNotFoundException;
import com.miranda.voting.assemblies.v1.repository.AssembliesRepository;
import com.miranda.voting.assemblies.v1.repository.AssociateRepository;
import com.miranda.voting.assemblies.v1.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AssembliesService {

    private final AssembliesRepository assembliesRepository;
    private final AssociateRepository associateRepository;
    private final ScheduleRepository scheduleRepository;

    public AssembliesService(AssembliesRepository assembliesRepository, AssociateRepository associateRepository, ScheduleRepository scheduleRepository) {
        this.assembliesRepository = assembliesRepository;
        this.associateRepository = associateRepository;
        this.scheduleRepository = scheduleRepository;
    }


    private void createSchedule(ScheduleDto request){
        ScheduleEntity entity = new ScheduleEntity();
        List<AssembliesEntity> assemblies = new ArrayList<>();
        entity.setTitle(request.getTitle());
        AssembliesEntity a = new AssembliesEntity();
        if (request.getTime() == null){
            a.setTime(LocalDateTime.now().plusMinutes(1));
        }else {
            a.setTime(request.getTime());
        }
        assemblies.add(a);
        entity.setAssembliesEntities(assemblies);

        scheduleRepository.save(entity);
    }

    private VotingSlipDto vote(AssembliesDto assemblies) throws ClassNotFoundException {
        Optional<AssembliesEntity> assembliesEntity = assembliesRepository.findById(assemblies.getAssemblieId());
        Optional<ScheduleEntity> scheduleEntity = scheduleRepository.findById(assemblies.getScheduleId());
        Optional<AssociateEntity> associateEntity = associateRepository.findById(assemblies.getAssociateId());
        if (assembliesEntity.isEmpty()){
            createAssemblies(assemblies.getScheduleId(), assemblies.getTime());
            scheduleEntity.get().setAssembliesEntities(assembliesRepository.findAll());
        }
        assembliesEntity = scheduleEntity.get().getAssembliesEntities().stream().findFirst();
        assembliesEntity.get().setTime(assemblies.getTime());
        assembliesEntity.get().setVote(assemblies.getVote());
        assembliesEntity.get().setAssociate(associateEntity.get());
        assembliesEntity.get().setSchedule(scheduleEntity.get());

        scheduleEntity.get().setAssembliesEntities(assembliesEntity.stream().toList());
        scheduleRepository.save(scheduleEntity.get());

        VotingSlipDto response = new VotingSlipDto();
        response.setDateVote(LocalDateTime.now());
        response.setNameAssociate(associateEntity.get().getName());
        return response;
    }

    private void createAssemblies(Long scheduleId, LocalDateTime time){
        Optional<ScheduleEntity> schedule = scheduleRepository.findById(scheduleId);
        if (schedule.isEmpty()){
            throw new ResourceNotFoundException("Schedule is not exist");
        }
        List<AssembliesEntity> assembliesEntity =  new ArrayList<>();
        AssembliesEntity entity = new AssembliesEntity();
        entity.setTime(time);
        assembliesEntity.add(entity);
        schedule.get().setAssembliesEntities(assembliesEntity);
        scheduleRepository.save(schedule.get());
    }

    private ResultDto polling(Long scheduleId, Long assembliesId){
        Optional<AssembliesEntity> assembliesEntity = assembliesRepository.findById(assembliesId);
        Optional<ScheduleEntity> scheduleEntity = scheduleRepository.findById(scheduleId);

        if (assembliesEntity.isEmpty() || scheduleEntity.isEmpty()){
            throw new ResourceNotFoundException("Schedule or Assemblies were not found ");
        }
        Integer totalVotes = scheduleEntity.get().getAssembliesEntities().size();
        Long upvote = scheduleEntity.get().getAssembliesEntities().stream().filter(AssembliesEntity::getVote).count();
        Double result = Double.longBitsToDouble(totalVotes/upvote);
        scheduleEntity.get().setResult(result);
        scheduleEntity.get().setTotalVotes(totalVotes);
        scheduleRepository.save(scheduleEntity.get());
        ResultDto dto = new ResultDto();
        dto.setResult(result);
        dto.setTotalVotes(totalVotes);
        return dto;
    }

    private void createAssociate(CreateAssociateDto req){
        AssociateEntity entity = new AssociateEntity();
        entity.setCpf(req.getCpf());
        entity.setName(req.getName());
        associateRepository.save(entity);
    }
}
