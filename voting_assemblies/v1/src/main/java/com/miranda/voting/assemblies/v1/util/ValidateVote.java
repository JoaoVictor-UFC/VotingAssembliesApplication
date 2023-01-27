package com.miranda.voting.assemblies.v1.util;

import com.miranda.voting.assemblies.v1.enums.ValidateVoteStatus;
import com.miranda.voting.assemblies.v1.errorExceptions.ResourceUnauthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ValidateVote {
    public static ValidateVoteStatus validateVote(String cpfCnpj){
        try {
            RestTemplate restTemplate = new RestTemplate();
            String fooResourceUrl
                    = "http://localhost:7003/validate/cpfCnpj/";
            ResponseEntity<Boolean> response = restTemplate.getForEntity(fooResourceUrl + cpfCnpj, Boolean.class);
            if (Boolean.TRUE.equals(response.getBody())){
                return ValidateVoteStatus.ABLE_TO_VOTE;
            }else {
                return ValidateVoteStatus.UNABLE_TO_VOTE;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new ResourceUnauthorizedException("Connection refused");
        }
    }
}
