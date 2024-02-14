package com.ssafy.dokidog2.user.repository;

import com.ssafy.dokidog2.user.entity.Pet;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    Pet findByPetId(Long id);
    @Query("select r.pet.petId from Relation r where r.user.userId = :userId and r.prime = true")
    List<Pet> findMyPets(Long userId);
}
