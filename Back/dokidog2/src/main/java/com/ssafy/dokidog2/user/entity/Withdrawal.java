package com.ssafy.dokidog2.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;

@Entity
@Getter
public class Withdrawal {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long withdrawalId;

    private String reason;
    private String feedback;

    // 탈퇴한 사람, 시간
    private Long regUsrSeq;
    private LocalDateTime regDttm;
    // 탈퇴취소 여부, 사람, 시간
    private Boolean delYN;
    private Long modUsrSeq;
    private LocalDateTime modDttm;
}
