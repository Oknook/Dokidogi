package com.ssafy.dokidog2.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Entity
@Getter
public class Relation {
    @Id @GeneratedValue
    @Column(name = "RELATION_ID")
    private long relationId;

    @OneToMany(mappedBy = "relation")
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "relation")
    private List<Pet> pets = new ArrayList<>();

    // 주양육자여부
    private boolean primary;

    private long regUsrSeq;
    private LocalDateTime regDttm;
    private boolean delYN;
    private long modUsrSeq;
    private LocalDateTime modDttm;
}
