package com.ssafy.dokidog2.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

@Entity(name = "pet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pet {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PET_ID")
    private long petId;

    // 동물 등록 번호 or rfid_cd 번호
    private Long regNo;
    private Long rfidNo;
    private String name;
    // 성별, // true, false
    private Character sex;
    // 품종
    private String kind;
    // 중성화 여부, 6글자 (예시 : 미중성)
    private Boolean neuter;
    // 관할기관명, 200글자 (서울특별시 강남구)
    private String orgNm;

    ////////////////// api결과 /////////////////////

    // 강아지 나이, mysql (unsigned) tinyint 0~255 매핑
    private Integer age;
    // 주인이 적은 정보, 주의점 등등?
    private String info;
    // 크기 - 소,중,대, 상관없음 0 1 2 3
    private Integer size;
    // 활동점수
    private Integer point;
    private Long imageId;

    // 사망, 서비스 탈퇴 등등 주인 혹은 강아지 탈퇴처리
    private Boolean delYN;
    // 등록 시간
    private LocalDateTime regDttm;
    // 탈퇴처리 시간
    private LocalDateTime modDttm;
    // 안씀
    private Long regUsrSeq;
    private Long modUsrSeq;

    @OneToMany(mappedBy = "pet")
    private List<Relation> users = new ArrayList<>();
}
