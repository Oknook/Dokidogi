package com.ssafy.dokidog2.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutPetDTO {

    private Long id;
    // 강아지 나이
    private Integer age;
    // 주인이 적은 정보, 주의점 등등?
    private String info;
    // 크기 - 소,중,대, 상관없음 0 1 2 3
    private Integer size;
    private Long imageId;
}
