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
import lombok.Getter;

@Entity(name = "pet")
@Getter
//@SequenceGenerator(
//    name = "PET_SEQ_GENERATOR",
//    sequenceName = "PET_SEQ"
//)
public class Pet {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PET_ID")
    private long petId;

    // 동물 등록 번호 or rfid_cd 번호
    private Long regNo;
    private Long rfidNo;
    private String name;
    // 성별, 4글자 (예시 : 암컷)
    private Boolean sex;
    // 품종
    private String kind;
    // 중성화 여부, 6글자 (예시 : 미중성)
    private Boolean neuter;
    // 관할기관명, 200글자 (서울특별시 강남구)
    private String orgNm;

    ////////////////// api결과 /////////////////////

    // 강아지 나이, mysql (unsigned) tinyint 0~255 매핑
    @Column(columnDefinition = "tinyint unsigned")
    private Byte age;
    // 주인이 적은 정보, 주의점 등등?
    private String info;
    // 크기 - 소,중,대, 상관없음 0 1 2 3
    private Byte size;
    // 활동점수
    private Integer point;
    private Long imageId;

    // 사망, 서비스 탈퇴 등등 주인 혹은 강아지 탈퇴처리
    private Boolean delYN;
    // 등록 시간
    private LocalDateTime regDttm;
    // 등록 유저 정보
    private Long regUsrSeq;
    // 탈퇴처리 시간
    private LocalDateTime modDttm;
    private Long modUsrSeq;

    @OneToMany(mappedBy = "pet")
    private List<Relation> users = new ArrayList<>();
}
