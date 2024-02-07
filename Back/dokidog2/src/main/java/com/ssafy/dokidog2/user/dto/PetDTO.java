package com.ssafy.dokidog2.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PetDTO {
    private Long id;
    private String name;
    private Boolean sex;
    private String kind;
    private Boolean neuter;
    private Byte age;
    private String info;
    private Byte size;
}
