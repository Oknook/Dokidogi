package com.ssafy.dokidog2.map.entity;


import com.ssafy.dokidog2.board.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "markerfile_table")
public class MarkerFileEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long markerFileId;

    @Column
    private String markerOriginalFileName;

    @Column
    private String markerStoredFileName;

    @Column
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marker_id")
    private MarkerEntity markerEntity;

    public static MarkerFileEntity toMarkerFileEntity(MarkerEntity markerEntity,
        String markerOriginalFileName, String markerStoredFileName, String imgUrl) {
        MarkerFileEntity markerFileEntity = new MarkerFileEntity();
        markerFileEntity.setMarkerOriginalFileName(markerOriginalFileName);
        markerFileEntity.setMarkerStoredFileName(markerStoredFileName);
        markerFileEntity.setMarkerEntity(markerEntity);
        markerFileEntity.setImgUrl(imgUrl);
        return markerFileEntity;
    }
}
