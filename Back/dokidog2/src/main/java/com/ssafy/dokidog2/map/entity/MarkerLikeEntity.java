package com.ssafy.dokidog2.map.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "markerLikes_table")
public class MarkerLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long markerLikeid;

    @ManyToOne
    @JoinColumn(name = "marker_id")
    private MarkerEntity markerEntity;

}
