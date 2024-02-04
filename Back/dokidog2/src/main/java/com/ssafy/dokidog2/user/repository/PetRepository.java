package com.ssafy.dokidog2.user.repository;

import com.ssafy.dokidog2.user.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

}
