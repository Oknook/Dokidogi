package com.ssafy.dokidog2.user.entity;

import com.ssafy.dokidog2.util.UserGrade;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
    private UserGrade grade;

    private String nickname;
    private String email;
    // 성별, tinyint(1)와 매핑 - 0, 1 // null 도 들어갈 수 있다
    private Character sex;
    // 생년월일(동물등록 정보조회 서비스 이용 시 필요, 이름으로 한다고 하면 어떻게 하지?)
    private String birth;
    private Double latitude;
    private Double longitude;

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

    public int calculateAge(String  birth) {
        String birthDateStr = birth;

        // YYMMDD 형식의 생년월일을 LocalDate 객체로 파싱
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        LocalDate birthDateLocal = LocalDate.parse(birthDateStr, formatter);

        // 2000년 이전과 이후의 구분 처리
        // 예를 들어, 990101은 1999년 1월 1일이 되어야 하고, 010101은 2001년 1월 1일이 되어야 합니다.
        // 만약 파싱된 날짜가 미래 날짜로 계산된다면, 100년을 빼서 올바른 날짜로 조정합니다.
        if (birthDateLocal.isAfter(LocalDate.now())) {
            birthDateLocal = birthDateLocal.minusYears(100);
        }

        // 오늘 날짜를 기준으로 나이 계산
        long age = ChronoUnit.YEARS.between(birthDateLocal, LocalDate.now());
        return (int) age;
    }
}
