package com.miranda.voting.assemblies.v1.entity;

import jakarta.persistence.*;
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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinColumn(name ="schedule_id")
    private List<AssembliesEntity> assembliesEntities;

}
