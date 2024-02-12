package com.ssafy.dokidog2.user.dto;

import lombok.Getter;

@Getter
public class PutUserDTO {
    private String nickname;
    private String email;
    private Character sex;
    private String birth;
    private Double latitude;
    private Double longitude;
}
