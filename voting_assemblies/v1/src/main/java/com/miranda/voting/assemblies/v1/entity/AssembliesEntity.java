package com.miranda.voting.assemblies.v1.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class AssembliesEntity extends AbstractEntity<Long> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Boolean vote;

    @OneToOne
    @JoinColumn(name ="id_associate", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_associate"))
    private AssociateEntity associate;

    @OneToOne
    @JoinColumn(name ="id_schedule", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_schedule"))
    private ScheduleEntity schedule;

}
