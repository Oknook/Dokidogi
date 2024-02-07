package com.ssafy.dokidog2.map.repository;

import com.ssafy.dokidog2.map.entity.MarkerCommentEntity;
import com.ssafy.dokidog2.map.entity.MarkerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarkerCommentRepository extends JpaRepository<MarkerCommentEntity, Long> {

    // 마커에 달린 댓글 오름차순
    List<MarkerCommentEntity> findAllByMarkerEntityOrderByMarkerCommentIdAsc(
        MarkerEntity markerEntity);

}
