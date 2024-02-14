package com.ssafy.dokidog2.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SocialDTO {
    private String companyId;
    private String companyName;
}
