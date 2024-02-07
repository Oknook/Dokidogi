package com.ssafy.dokidog2.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity(name = "users")
@Getter
@Setter
//@SequenceGenerator(
//    name = "USER_SEQ_GENERATOR",
//    sequenceName = "USER_SEQ"
//)
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
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
    private Boolean sex;
    // 생년월일(동물등록 정보조회 서비스 이용 시 필요, 이름으로 한다고 하면 어떻게 하지?)
    private Integer birth;
    private String address;

    private Long imageId;
    private Integer point;

    // 가입시간
    private LocalDateTime regDttm;
    // 탈퇴처리 여부
    @ColumnDefault("0")
    private Boolean delYN;
    // 직접 로그인 시 비밀번호 변경이력으로 두면 좋을 듯
    // 현재는 탈퇴 날짜
    private LocalDateTime modDttm;

    // 안쓸 것 같긴 함.
    private Long regUsrSeq;
    private Long modUsrSeq;

    @OneToMany(mappedBy = "user")
    private List<Relation> pets = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Grass> grasses = new ArrayList<>();
    public void addGrass(LocalDateTime dateTime) {
        Grass grass = new Grass(this, dateTime);
        this.grasses.add(grass);
    }
    
    // 랭크, 뱃지랑 관계
    private String ranking;
    private Long badgeId;
}
