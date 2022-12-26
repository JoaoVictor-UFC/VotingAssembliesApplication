package com.miranda.voting.assemblies.v1.repository;

import com.miranda.voting.assemblies.v1.entity.AssembliesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AssembliesRepository extends JpaRepository<AssembliesEntity, Long>{

    @Query("SELECT u FROM AssembliesEntity u WHERE u.time > ?1")
    List<AssembliesEntity> timeIsValid(LocalDateTime time);
}
