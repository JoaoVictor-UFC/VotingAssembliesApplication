package com.miranda.voting.assemblies.v1.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class AssembliesEntity extends AbstractEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;

    private String type;

    
}
