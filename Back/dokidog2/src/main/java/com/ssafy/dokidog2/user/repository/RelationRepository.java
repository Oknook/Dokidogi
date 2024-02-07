package com.ssafy.dokidog2.user.repository;

import com.ssafy.dokidog2.user.entity.Pet;
import com.ssafy.dokidog2.user.entity.Relation;
import com.ssafy.dokidog2.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationRepository extends JpaRepository<Relation, Long> {
    @Query("SELECT r.pet FROM Relation r WHERE r.user.userId = :userId")
    List<Pet> findPetsByUserId(@Param("userId") Long userId);
}
