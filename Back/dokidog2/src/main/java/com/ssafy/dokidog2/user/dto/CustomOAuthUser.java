package com.ssafy.dokidog2.user.dto;

import com.ssafy.dokidog2.util.UserGrade;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomOAuthUser {
    private Long userId;
    private String companyId;
    private String companyName;
    private UserGrade role;
}
