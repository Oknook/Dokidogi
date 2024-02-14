package com.ssafy.dokidog2.user.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PetAPIDTO {
    private Long petId;
    private Long regNo;
    private Long rfidNo;
    private String name;
    private Character sex;
    private String kind;
    private String neuter;
    private String orgNm;
}
