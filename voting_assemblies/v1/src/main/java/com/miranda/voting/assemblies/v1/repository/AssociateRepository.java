package com.miranda.voting.assemblies.v1.repository;

import com.miranda.voting.assemblies.v1.entity.AssociateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociateRepository extends JpaRepository<AssociateEntity, Long> {

}
