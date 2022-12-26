package com.miranda.voting.assemblies.v1.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class ScheduleEntity extends AbstractEntity<Long> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String title;

    private Integer totalVotes;

    private Double result;

    @OneToMany
    private List<AssembliesEntity> assembliesEntities;

}
