package com.ssafy.dokidog2.user.service;

import com.ssafy.dokidog2.user.dto.PetDTO;
import com.ssafy.dokidog2.user.entity.Pet;
import com.ssafy.dokidog2.user.repository.PetRepository;
import com.ssafy.dokidog2.user.repository.RelationRepository;
import org.springframework.stereotype.Service;

@Service
public class PetService {
    private final PetRepository petRepository;
    private final RelationRepository relationRepository;
    public PetService(PetRepository petRepository, RelationRepository relationRepository) {
        this.petRepository = petRepository;
        this.relationRepository = relationRepository;
    }

    public PetDTO getPetProfile(long petId) {
        PetDTO petDTO = new PetDTO();
        Pet pet = petRepository.findById(petId).orElse(null);
        if (pet.getName() != null) petDTO.setName(pet.getName());
        if (pet.getSex() != null) petDTO.setSex(pet.getSex());
        if (pet.getKind() != null) petDTO.setKind(pet.getKind());
        if (pet.getNeuter() != null) petDTO.setNeuter(pet.getNeuter());
        if (pet.getAge() != null) petDTO.setAge(pet.getAge());
        if (pet.getInfo() != null) petDTO.setInfo(pet.getInfo());
        if (pet.getSize() != null) petDTO.setSize(pet.getSize());
        return petDTO;
    }
}
