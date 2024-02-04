package com.ssafy.dokidog2.user.repository;

import com.ssafy.dokidog2.user.entity.Relation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationRepository extends JpaRepository<Relation, Long> {

}
