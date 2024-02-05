package com.ssafy.dokidog2.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Entity(name = "pet")
@Getter
public class Pet {
    @Id @GeneratedValue
    @Column(name = "PET_ID")
    private Long petId;

    // 동물 등록 번호 or rfid_cd 번호
    private Long regNo;
    private Long rfidNo;
    private String name;
    // 성별, 4글자 (예시 : 암컷)
    private boolean sex;
    // 품종
    private String kind;
    // 중성화 여부, 6글자 (예시 : 미중성)
    private boolean neuter;
    // 관할기관명, 200글자 (서울특별시 강남구)
    private String orgNm;

    ////////////////// api결과 /////////////////////

    // 강아지 나이, mysql (unsigned) tinyint 0~255 매핑
    @Column(columnDefinition = "tinyint unsigned")
    private byte age;
    // 주인이 적은 정보, 주의점 등등?
    private String info;
    // 크기 - 소,중,대
    private byte size;
    // 활동점수
    private int point;
    private long imageId;

    // 사망, 서비스 탈퇴 등등 주인 혹은 강아지 탈퇴처리
    private boolean delYN;
    // 등록 시간
    private LocalDateTime regDttm;
    // 등록 유저 정보
    private long regUsrSeq;
    // 탈퇴처리 시간
    private LocalDateTime modDttm;
    private long modUsrSeq;

    @OneToMany(mappedBy = "pet")
    private List<Relation> users = new ArrayList<>();
//    @ManyToOne
//    private Relation owner;
}
