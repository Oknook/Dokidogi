package com.ssafy.dokidog2.user.dto;

import com.ssafy.dokidog2.user.entity.Pet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PetDTO {
    private Long id;
    // 애견 이름
    private String name;
    // 성별, 0, 1, null
    private Boolean sex;
    // 품종
    private String kind;
    // 중성화 여부
    private Boolean neuter;
    // 애견 나이
    private Byte age;
    // 주인이 적은 정보, 주의점 등등?
    private String info;
    // 크기 - 소,중,대, 상관없음 0 1 2 3
    private Byte size;

    public PetDTO(Pet pet){
        this.id = pet.getPetId();
        this.name = pet.getName();
        this.sex = pet.getSex();
        this.kind = pet.getKind();
        this.age = pet.getAge();
        this.size = pet.getSize();
        this.info = pet.getInfo();
        this.neuter = pet.getNeuter();
    }
}
