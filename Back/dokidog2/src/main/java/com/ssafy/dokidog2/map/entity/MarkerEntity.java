package com.ssafy.dokidog2.map.entity;



import com.ssafy.dokidog2.board.entity.BaseEntity;
import com.ssafy.dokidog2.map.dto.MarkerDTO;
import com.ssafy.dokidog2.user.entity.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "marker_table")
public class MarkerEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long markerId;

    @Column(length = 20) // 크기 20, null 가능
    private String markerWriter;

    @Column
    private Double markerLatitude;

    @Column
    private Double markerLongitude;

    @Column
    private String markerTitle;

    @Column
    private String markerContents;

    @Column
    private String imgUrl;

    @Column
    private int markerLikes;

    @Column
    private int markerFileAttached; // 1 or 0


    @OneToMany(mappedBy = "markerEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<WalkPostEntity> walkPostEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "markerEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MarkerCommentEntity> markerCommentEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "markerEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MarkerFileEntity> markerFileEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "markerEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MarkerLikeEntity> markerLikeEntityList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static MarkerEntity toSaveMarkerEntity(MarkerDTO markerDTO, User user) {
        MarkerEntity markerEntity = new MarkerEntity();
        markerEntity.setUser(user);
        markerEntity.setMarkerWriter(user.getNickname());
        markerEntity.setMarkerId(markerDTO.getMarkerId());
        markerEntity.setMarkerTitle(markerDTO.getMarkerTitle());
        markerEntity.setMarkerLatitude(markerDTO.getMarkerLatitude());
        markerEntity.setMarkerLongitude(markerDTO.getMarkerLongitude());
        markerEntity.setMarkerContents(markerDTO.getMarkerContents());
        markerEntity.setMarkerFileAttached(0); // 파일 없음.
        markerEntity.setMarkerLikes(markerDTO.getMarkerLikes());
        return markerEntity;
    }

    public static MarkerEntity toSaveFileMarkerEntity(MarkerDTO markerDTO, User user) {
        MarkerEntity markerEntity = new MarkerEntity();
        markerEntity.setUser(user);
        markerEntity.setMarkerWriter(user.getNickname());
        markerEntity.setMarkerId(markerDTO.getMarkerId());
        markerEntity.setMarkerTitle(markerDTO.getMarkerTitle());
        markerEntity.setMarkerContents(markerDTO.getMarkerContents());
        markerEntity.setMarkerLatitude(markerDTO.getMarkerLatitude());
        markerEntity.setMarkerLongitude(markerDTO.getMarkerLongitude());
        markerEntity.setMarkerFileAttached(1); // 파일 있음.
        markerEntity.setMarkerLikes(markerDTO.getMarkerLikes());
        return markerEntity;
    }

    public static MarkerEntity toUpdateMarkerEntity(MarkerDTO markerDTO) {
        MarkerEntity markerEntity = new MarkerEntity();
        markerEntity.setMarkerTitle(markerDTO.getMarkerTitle());
        markerEntity.setMarkerContents(markerDTO.getMarkerContents());
        markerEntity.setMarkerContents(markerDTO.getMarkerContents());
        markerEntity.setMarkerLatitude(markerDTO.getMarkerLatitude());
        markerEntity.setMarkerFileAttached(0); // 파일 없음.
        markerEntity.setMarkerLikes(markerDTO.getMarkerLikes());
        return markerEntity;
    }

}
