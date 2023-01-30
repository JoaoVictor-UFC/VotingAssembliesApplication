package com.miranda.voting.assemblies.v1.repository;

import com.miranda.voting.assemblies.v1.entity.AssembliesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssembliesRepository extends JpaRepository<AssembliesEntity, Long>{

    @Query(value = "select * from assemblies_entity a where a.id_schedule = ?1", nativeQuery = true)
    List<AssembliesEntity> findAllByScheduleId(Long id);

    @Query(value = "select * from assemblies_entity where id_associate = ?1", nativeQuery = true)
    boolean existsByAssociateId(Long associateId);
}
