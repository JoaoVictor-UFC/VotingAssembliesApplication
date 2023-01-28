package com.miranda.voting.assemblies.v1.util;

import com.miranda.voting.assemblies.v1.enums.ValidateVoteStatus;
import com.miranda.voting.assemblies.v1.errorExceptions.ResourceUnauthorizedException;
import com.miranda.voting.assemblies.v1.repository.ScheduleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ValidateVoteTest {
    @Mock
    private RestTemplate restTemplate;

    private static final String URL = "localhost:7003/validate/cpfCnpj/";

    private static final String CPF = "02282660099";

    @Test
    void whenRestTemplateReturnsFalseMustReturnUnableVote() {

        ValidateVote validateVote = new ValidateVote(URL, restTemplate);

        Mockito.when(this.restTemplate.getForEntity( URL + CPF, Boolean.class))
                .thenReturn(new ResponseEntity<>(false, HttpStatus.OK));

        ValidateVoteStatus res = validateVote.validateVote(CPF);
        Assertions.assertEquals(res, ValidateVoteStatus.UNABLE_TO_VOTE);
    }

    @Test
    void whenRestTemplateReturnsTrueMustReturnAbleVote() {

        ValidateVote validateVote = new ValidateVote(URL , restTemplate);

        Mockito.when(this.restTemplate.getForEntity(URL + CPF, Boolean.class))
                .thenReturn(new ResponseEntity<>(true, HttpStatus.OK));

        ValidateVoteStatus res = validateVote.validateVote(CPF);
        Assertions.assertEquals(res, ValidateVoteStatus.ABLE_TO_VOTE);
    }

    @Test
    void whenRestTemplateReturnsException() {

        ValidateVote validateVote = new ValidateVote(URL , restTemplate);

        Mockito.when(this.restTemplate.getForEntity(URL + CPF, Boolean.class))
                .thenThrow(new ResourceUnauthorizedException("Connection refused"));

        assertThrows(ResourceUnauthorizedException.class, () -> {
            validateVote.validateVote(CPF);
        });
    }
}