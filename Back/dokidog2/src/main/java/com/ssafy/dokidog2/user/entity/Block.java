package com.ssafy.dokidog2.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// https://lealea.tistory.com/195
public class Block {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long blockId;

    @ManyToOne
    @JoinColumn(name = "blocking_id")
    private User blocking;

    @ManyToOne
    @JoinColumn(name = "blocked_id")
    private User blocked;

    // 차단한 사람, 차단 당한사람
    private Long regUsrSeq;
    private LocalDateTime regDttm;
    private Boolean delYN;
    private Long modUsrSeq;
    private LocalDateTime modDttm;
}
