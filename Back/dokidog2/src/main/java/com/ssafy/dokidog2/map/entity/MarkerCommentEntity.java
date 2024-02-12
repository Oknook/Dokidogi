package com.ssafy.dokidog2.map.entity;


import com.ssafy.dokidog2.board.entity.BaseEntity;
import com.ssafy.dokidog2.map.dto.MarkerCommentDTO;
import com.ssafy.dokidog2.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "markerComment_table")
public class MarkerCommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long markerCommentId;

    @Column
    private String markerCommentWriter;

    @Column
    private String markerCommentContents;

    /* marker:markerComment = 1:N */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marker_id")
    private MarkerEntity markerEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public static MarkerCommentEntity toSaveMarkerCommentEntity(MarkerCommentDTO markerCommentDTO,
        MarkerEntity markerEntity, User user) {
        MarkerCommentEntity markerCommentEntity = new MarkerCommentEntity();
        markerCommentEntity.setUser(user);
        markerCommentEntity.setMarkerCommentWriter(markerCommentDTO.getMarkerCommentWriter());
        markerCommentEntity.setMarkerCommentContents(markerCommentDTO.getMarkerCommentContents());
        markerCommentEntity.setMarkerEntity(markerEntity);
        return markerCommentEntity;
    }
}
