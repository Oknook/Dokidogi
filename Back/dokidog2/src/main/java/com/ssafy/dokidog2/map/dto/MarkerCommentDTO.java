package com.ssafy.dokidog2.map.dto;

import com.ssafy.dokidog2.map.entity.MarkerCommentEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MarkerCommentDTO {

    private Long markerCommentId;
    private String markerCommentWriter;
    private String markerCommentContents;
    private Long markerId;
    private LocalDateTime markerCommentCreatedTime;

    public static MarkerCommentDTO toMarkerCommentDTO(MarkerCommentEntity markerCommentEntity,
        Long markerId) {
        MarkerCommentDTO markerCommentDTO = new MarkerCommentDTO();
        markerCommentDTO.setMarkerCommentId(markerCommentEntity.getMarkerCommentId());
        markerCommentDTO.setMarkerCommentWriter(markerCommentEntity.getMarkerCommentWriter());
        markerCommentDTO.setMarkerCommentContents(markerCommentEntity.getMarkerCommentContents());
        markerCommentDTO.setMarkerCommentCreatedTime(markerCommentEntity.getCreatedTime());
        markerCommentDTO.setMarkerId(markerId);
        return markerCommentDTO;
    }
}
