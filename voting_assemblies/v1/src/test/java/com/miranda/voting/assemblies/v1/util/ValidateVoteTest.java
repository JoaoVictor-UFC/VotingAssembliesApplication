package com.miranda.voting.assemblies.v1.util;

import com.miranda.voting.assemblies.v1.enums.ValidateVoteStatus;
import com.miranda.voting.assemblies.v1.errorExceptions.ResourceUnauthorizedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateVoteTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ValidateVote validateVote;

    private static final String URL = "http://localhost:7003/validate/cpfCnpj/123456789";
    private static final String CPF = "123456789";

    @Test
    public void testValidateVote_ableToVote() {
        lenient().when(restTemplate.getForEntity(URL , Boolean.class))
                .thenReturn(ResponseEntity.ok(true));

        lenient().when(this.restTemplate.getForEntity( URL + CPF, Boolean.class))
                .thenReturn(new ResponseEntity<>(false, HttpStatus.OK));
        assertEquals(ValidateVoteStatus.ABLE_TO_VOTE, ValidateVoteStatus.ABLE_TO_VOTE);
    }

    @Test
    public void testValidateVote_unableToVote() {
        lenient().when(restTemplate.getForEntity(URL , Boolean.class))
                .thenReturn(ResponseEntity.ok(false));

        lenient().when(this.restTemplate.getForEntity(URL + CPF, Boolean.class))
                .thenThrow(new ResourceUnauthorizedException("Schedule not found", null));
        assertEquals(ValidateVoteStatus.UNABLE_TO_VOTE, ValidateVoteStatus.UNABLE_TO_VOTE);
    }
}