package com.ssafy.dokidog2.map.entity;

import com.ssafy.dokidog2.board.entity.BaseEntity;
import com.ssafy.dokidog2.map.dto.WalkPostDTO;
import com.ssafy.dokidog2.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "walkPost_table")
public class WalkPostEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walkPostId;

    @Column
    private String walkPostTitle;

    @Column
    private String walkPostDate;

    @Column
    private String walkPostTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marker_id")
    private MarkerEntity markerEntity;

    @Column(name = "walk_start")
    private String walkStart;

    @Column(name = "walk_end")
    private String walkEnd;

    @Column
    private String walkPostWriter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // MarkerEntity를 받아 WalkPostEntity 객체를 생성하고 markerTitle을 설정하는 메소드
    public static WalkPostEntity toSaveWalkPostEntity(WalkPostDTO walkPostDTO, MarkerEntity markerEntity, User user) {
        WalkPostEntity walkPostEntity = new WalkPostEntity();
        walkPostEntity.setUser(user);
        walkPostEntity.setMarkerEntity(markerEntity);
        walkPostEntity.setWalkStart(walkPostDTO.getWalkStart());
        walkPostEntity.setWalkEnd(walkPostDTO.getWalkEnd());
        walkPostEntity.setWalkPostTitle(walkPostDTO.getWalkPostTitle());
        walkPostEntity.setWalkPostDate(walkPostDTO.getWalkPostDate());
        walkPostEntity.setWalkPostTime(walkPostDTO.getWalkPostTime());
        walkPostEntity.setWalkPostWriter(walkPostDTO.getWalkPostWriter());
        // 여기에 더 필요한 필드가 있다면, DTO에서 엔티티로 데이터를 복사하세요.
        return walkPostEntity;
    }

}
