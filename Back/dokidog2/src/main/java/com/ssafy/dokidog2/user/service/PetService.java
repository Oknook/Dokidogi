package com.ssafy.dokidog2.user.service;

import com.ssafy.dokidog2.user.dto.PetDTO;
import com.ssafy.dokidog2.user.dto.PutPetDTO;
import com.ssafy.dokidog2.user.entity.Pet;
import com.ssafy.dokidog2.user.entity.Relation;
import com.ssafy.dokidog2.user.entity.User;
import com.ssafy.dokidog2.user.repository.PetRepository;
import com.ssafy.dokidog2.user.repository.RelationRepository;
import com.ssafy.dokidog2.user.repository.UserRepository;
import com.ssafy.dokidog2.util.Response;
import com.ssafy.dokidog2.util.UserGrade;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PetService {
    private final PetRepository petRepository;
    private final RelationRepository relationRepository;
    private final UserRepository userRepository;
    public PetService(PetRepository petRepository, RelationRepository relationRepository, UserRepository userRepository) {
        this.petRepository = petRepository;
        this.relationRepository = relationRepository;
        this.userRepository = userRepository;
    }

    public Response regular(long userId, PutPetDTO dto) {
        Response response = new Response();
        try {
            Pet pet = petRepository.findById(dto.getId()).orElse(null);
            pet.builder()
                .age(dto.getAge())
                .info(dto.getInfo())
                .size(dto.getSize())
                .imageId(dto.getImageId())
                .build();
            petRepository.save(pet);

            User user = userRepository.findByUserId(userId);
            if (!user.getGrade().equals(UserGrade.REGULAR)) user.setGrade(UserGrade.REGULAR);
            userRepository.save(user);

            response.builder()
                .code("200")
                .message("입력 성공")
                .build();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            response.builder()
                .code("400")
                .message("입력 오류")
                .build();
        }
        return response;
    }

    public Response modifyPet(PutPetDTO dto) {
        Response response = new Response();
        try {
            Pet pet = petRepository.findById(dto.getId()).orElse(null);
            if (dto.getAge() != null) pet.setAge(dto.getAge());
            if (dto.getInfo() != null) pet.setInfo(dto.getInfo());
            if (dto.getSize() != null) pet.setSize(dto.getSize());
            if (dto.getImageId() != null) pet.setImageId(dto.getImageId());
            petRepository.save(pet);

            response.builder()
                .code("200")
                .message("입력 성공")
                .build();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            response.builder()
                .code("400")
                .message("입력 오류")
                .build();
        }
        return response;
    }

    public Response getPetProfile(long petId) {
        Pet pet = petRepository.findById(petId).orElse(null);

        return Response.builder()
            .code("200")
            .message("반려동물 조회")
            .data(PetDTO.builder()
                .name(pet.getName())
                .sex(pet.getSex())
                .kind(pet.getKind())
                .neuter(pet.getNeuter())
                .orgNm(pet.getOrgNm())
                .age(pet.getAge())
                .info(pet.getInfo())
                .size(pet.getSize())
                .point(pet.getPoint())
                .imageId(pet.getImageId())
                .delYN(pet.getDelYN())
                .regDttm(pet.getRegDttm())
                .modDttm(pet.getModDttm())
                .owner(relationRepository.getOwner(petId).getUserId())
                .build())
            .build();
    }
}
