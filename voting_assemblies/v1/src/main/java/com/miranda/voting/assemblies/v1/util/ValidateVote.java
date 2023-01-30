package com.miranda.voting.assemblies.v1.util;

import com.miranda.voting.assemblies.v1.enums.ValidateVoteStatus;
import com.miranda.voting.assemblies.v1.errorExceptions.ResourceUnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ValidateVote {


    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateVote.class);
    private static final String ERROR_MESSAGE = "Unable to connect to the voting validation service.";

    @Value("${validate.vote}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    public ValidateVote(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    public ValidateVote(){}

    public ValidateVoteStatus validateVote(String cpfCnpj) {
        try {
            ResponseEntity<Boolean> response = restTemplate.getForEntity(url + cpfCnpj, Boolean.class);
            return getVoteStatus(response.getBody());
        } catch (Exception e) {
            LOGGER.error("An error occurred while checking vote status", e);
            throw new ResourceUnauthorizedException(ERROR_MESSAGE, e);
        }
    }

    private ValidateVoteStatus getVoteStatus(Boolean isAbleToVote) {
        return Boolean.TRUE.equals(isAbleToVote) ? ValidateVoteStatus.ABLE_TO_VOTE : ValidateVoteStatus.UNABLE_TO_VOTE;
    }
}
