package com.ssafy.dokidog2.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Entity
@Getter
public class User {
    @Id @GeneratedValue
    private long userId;

    // 소셜 로그인 회사에서 관리하는 아이디
    @Column(nullable = false)
    private String companyId;

    // 소셜 로그인 회사 이름
    @Column(nullable = false)
    private String companyName;

    private String nickname;
    private String email;
    // 성별, tinyint(1)와 매핑 - 0, 1
    private boolean sex;
    // 생년월일(동물등록 정보조회 서비스 이용 시 필요, 이름으로 한다고 하면 어떻게 하지?)
    private int birth;
    private String address;

    private long imageId;
    private int point;

    // 가입시간
    private LocalDateTime regDttm;
    // 탈퇴처리 여부
    private boolean delYN;
    // 직접 로그인 시 비밀번호 변경이력으로 두면 좋을 듯
    // 현재는 탈퇴 날짜
    private LocalDateTime modDttm;

    // 안쓸 것 같긴 함.
    private long regUsrSeq;
    private long modUsrSeq;

    @ManyToOne
    @JoinColumn(name = "RELATION_ID")
    private Relation relation;

    @OneToMany(mappedBy = "blocking")
    private List<Block> blockingList;

    @OneToMany(mappedBy = "blocked")
    private List<Block> blockedList;
    
    // 랭크, 뱃지랑 관계
    private String rank;
    private long badgeId;
}
