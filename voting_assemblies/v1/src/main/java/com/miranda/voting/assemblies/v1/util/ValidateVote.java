package com.miranda.voting.assemblies.v1.util;

import com.miranda.voting.assemblies.v1.enums.ValidateVoteStatus;
import com.miranda.voting.assemblies.v1.errorExceptions.ResourceUnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ValidateVote {

    @Value("${validate.vote}")
    private final String url;

    private final RestTemplate restTemplate;

    public ValidateVote(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    public ValidateVoteStatus validateVote(String cpfCnpj){
        try {
            ResponseEntity<Boolean> response = restTemplate.getForEntity(url + cpfCnpj, Boolean.class);
            if (Boolean.TRUE.equals(response.getBody())){
                return ValidateVoteStatus.ABLE_TO_VOTE;
            }
            return ValidateVoteStatus.UNABLE_TO_VOTE;
        }catch (Exception e){
            e.printStackTrace();
            throw new ResourceUnauthorizedException("Connection refused");
        }
    }
}
