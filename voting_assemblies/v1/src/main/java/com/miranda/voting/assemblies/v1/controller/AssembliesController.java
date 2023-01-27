package com.miranda.voting.assemblies.v1.controller;

import com.miranda.voting.assemblies.v1.dto.*;
import com.miranda.voting.assemblies.v1.entity.AssociateEntity;
import com.miranda.voting.assemblies.v1.entity.ScheduleEntity;
import com.miranda.voting.assemblies.v1.errorExceptions.MessageErrorCustom;
import com.miranda.voting.assemblies.v1.service.AssembliesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Api(value = "AssembliesController", tags = { "Assemblies Controller" })
@Valid
@RequestMapping("/assemblies")
@RestController
public class AssembliesController {

    private final AssembliesService service;

    public AssembliesController(AssembliesService service) {
        this.service = service;
    }

    @ApiOperation(value = "Create Associate")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = MessageErrorCustom.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = MessageErrorCustom.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = MessageErrorCustom.class)
    })
    @PostMapping(value = "/create/associate", consumes = { "application/json", "application/xml" })
    public ResponseEntity<?> createAssociate(@RequestBody @Valid CreateAssociateDto req){
        AssociateEntity associate = service.createAssociate(req);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}/id").buildAndExpand(associate.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @ApiOperation(value = "Create Schedule")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = MessageErrorCustom.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = MessageErrorCustom.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = MessageErrorCustom.class)
    })
    @PostMapping(value = "/create/schedule", consumes = { "application/json", "application/xml" })
    public ResponseEntity<?> createSchedule(@RequestBody @Valid CreateScheduleDto req){
        ScheduleEntity schedule = service.createSchedule(req);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}/id").buildAndExpand(schedule.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @ApiOperation(value = "Voting Slip")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = VoteDto.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = MessageErrorCustom.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = MessageErrorCustom.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = MessageErrorCustom.class)
    })
    @PostMapping(value = "/vote", consumes = { "application/json", "application/xml" })
    public ResponseEntity<VotingSlipDto> votingSlip(@RequestBody @Valid VoteDto req){
        return ResponseEntity.ok().body(service.votingSlip(req));
    }

    @ApiOperation(value = "Polling")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED", response = String.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = MessageErrorCustom.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = MessageErrorCustom.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = MessageErrorCustom.class)
    })
    @GetMapping(value = "/result/{scheduleId}", produces = { "application/json", "application/xml" })
    public ResponseEntity<ResultDto> polling(@PathVariable Long scheduleId){
        return ResponseEntity.ok().body(service.polling(scheduleId));
    }
}
