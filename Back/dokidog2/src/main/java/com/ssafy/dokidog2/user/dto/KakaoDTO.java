package com.ssafy.dokidog2.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class KakaoDTO {
    private long companyId;
    private String companyName;
}
