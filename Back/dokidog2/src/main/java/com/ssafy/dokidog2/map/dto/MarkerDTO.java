package com.ssafy.dokidog2.map.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.dokidog2.map.entity.MarkerEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MarkerDTO {

    private Long markerId;
    private Long userId;
    private Double markerLatitude;
    private Double markerLongitude;
    private String markerWriter;
    private String markerTitle;
    private String markerContents;
    private LocalDateTime markerCreatedTime;
    private LocalDateTime markerUpdatedTime;
    private int markerLikes;

    private int markerFileAttached; // 파일 첨부 여부(첨부 1, 미첨부 0)

    private String imgUrl;
    @JsonIgnore
    private MultipartFile markerBoardFile; // save.html -> Controller 파일 담는 용도
    private String markerOriginalFileName; // 원본 파일 이름
    private String markerStoredFileName; // 서버 저장용 파일 이름

    // 조회용 DTO
    public static MarkerDTO toMarkerDTO(MarkerEntity markerEntity) {
        MarkerDTO markerDTO = new MarkerDTO();
        markerDTO.setMarkerId(markerEntity.getMarkerId());
        markerDTO.setUserId(markerEntity.getUser().getUserId());
        markerDTO.setMarkerWriter(markerEntity.getMarkerWriter());
        markerDTO.setMarkerLatitude(markerEntity.getMarkerLatitude());
        markerDTO.setMarkerLongitude(markerEntity.getMarkerLongitude());
        markerDTO.setMarkerTitle(markerEntity.getMarkerTitle());
        markerDTO.setMarkerContents(markerEntity.getMarkerContents());
        markerDTO.setMarkerLikes(markerEntity.getMarkerLikes());
        markerDTO.setMarkerCreatedTime(markerEntity.getCreatedTime());
        markerDTO.setMarkerUpdatedTime(markerEntity.getUpdatedTime());

        if (markerEntity.getMarkerFileAttached() == 0) {
            markerDTO.setMarkerFileAttached(markerEntity.getMarkerFileAttached());
        } else {
            List<String> markerOriginalFileNameList = new ArrayList<>();
            List<String> markerStoredFileNameList = new ArrayList<>();
            markerDTO.setMarkerFileAttached(markerEntity.getMarkerFileAttached());
            markerDTO.setMarkerOriginalFileName(
                markerEntity.getMarkerFileEntityList().get(0).getMarkerOriginalFileName());
            markerDTO.setMarkerStoredFileName(
                markerEntity.getMarkerFileEntityList().get(0).getMarkerStoredFileName());
        }
        return markerDTO;
    }
}
