package com.ssafy.dokidog2.map.dto;


import com.ssafy.dokidog2.map.entity.WalkPostEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class WalkPostDTO {

    private Long walkPostId;
    private String walkPostTitle;
    // String or LocalDateTime
    private String walkPostDate;
    private String walkPostTime;
    private Long markerId;
    private Long userId;
    private String walkStart;
    private String walkEnd;
    private String walkPostWriter;
    private LocalDateTime walkPostCreatedTime;

    public static WalkPostDTO toWalkPostDTO(WalkPostEntity walkPostEntity) {
        WalkPostDTO walkPostDTO = new WalkPostDTO();
        walkPostDTO.setWalkPostId(walkPostEntity.getWalkPostId());
        walkPostDTO.setWalkPostTitle(walkPostEntity.getWalkPostTitle());
        walkPostDTO.setWalkPostDate(walkPostEntity.getWalkPostDate());
        walkPostDTO.setWalkPostTime(walkPostEntity.getWalkPostTime());
        walkPostDTO.setMarkerId(walkPostEntity.getMarkerEntity() != null ? walkPostEntity.getMarkerEntity().getMarkerId() : null);
        walkPostDTO.setWalkStart(walkPostEntity.getWalkStart());
        walkPostDTO.setWalkEnd(walkPostEntity.getWalkEnd());
        walkPostDTO.setWalkPostWriter(walkPostEntity.getWalkPostWriter());
        // 여기에 더 많은 필드를 설정하세요.
        return walkPostDTO;
    }
}
