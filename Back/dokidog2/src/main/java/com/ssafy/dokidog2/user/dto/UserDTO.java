package com.ssafy.dokidog2.user.dto;

import com.ssafy.dokidog2.user.entity.Grass;
import com.ssafy.dokidog2.user.entity.Pet;
import com.ssafy.dokidog2.user.entity.Relation;
import com.ssafy.dokidog2.util.UserGrade;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.json.simple.JSONObject;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class UserDTO {
    private Long userId;
    private String companyId;
    private String companyName;
    private UserGrade grade;

    private String nickname;
    private String email;
    private Character sex;
    private String birth;
    private Double latitude;
    private Double longitude;

    private Long imageId;
    private Integer point;
    private JSONObject grass;
    private List<Pet> pets;


    // 가입시간
    private LocalDateTime regDttm;
    // 탈퇴처리 여부
    @ColumnDefault("0")
    private Boolean delYN;
    // 탈퇴 날짜
    private LocalDateTime modDttm;

    // 안쓸 것 같긴 함.
    private Long regUsrSeq;
    private Long modUsrSeq;
}
