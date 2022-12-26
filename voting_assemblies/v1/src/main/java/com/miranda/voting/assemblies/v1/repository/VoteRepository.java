package com.miranda.voting.assemblies.v1.repository;

import com.miranda.voting.assemblies.v1.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, Long> {

    @Query(value = "select * from vote_entity v where v.assemblie_id = ?1", nativeQuery = true)
    List<VoteEntity> findAllByAssemblieId(Long id);
}
