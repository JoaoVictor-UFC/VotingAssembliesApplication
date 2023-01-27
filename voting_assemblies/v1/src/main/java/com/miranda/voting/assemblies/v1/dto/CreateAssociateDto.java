package com.miranda.voting.assemblies.v1.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

@Data
public class CreateAssociateDto {

    @NotNull
    private String name;

//    @CPF
    @NotNull
    private String cpf;
}
