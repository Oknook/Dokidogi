package com.ssafy.dokidog2.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Entity
@Getter
public class Relation {

    @Id
    @GeneratedValue
    @Column(name = "RELATION_ID")
    private long relationId;


    // 연관관계
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "PET_ID")
    private Pet pet;
//    @OneToMany
//    private List<User> users = new ArrayList<>();
//
//    @OneToMany
//    private List<Pet> pets = new ArrayList<>();
    // 연관관계

    // 주양육자여부
    private boolean prime;

    private long regUsrSeq;
    private LocalDateTime regDttm;
    private boolean delYN;
    private long modUsrSeq;
    private LocalDateTime modDttm;
}
