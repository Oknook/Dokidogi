package com.ssafy.dokidog2.map.repository;

import com.ssafy.dokidog2.map.entity.WalkPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalkPostRepository extends JpaRepository<WalkPostEntity, Long> {
    List<WalkPostEntity> findAllByMarkerEntity_MarkerIdOrderByWalkPostIdAsc(Long markerId);
}