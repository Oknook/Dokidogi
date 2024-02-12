package com.ssafy.dokidog2.map.service;


import com.ssafy.dokidog2.map.dto.MarkerCommentDTO;
import com.ssafy.dokidog2.map.entity.MarkerCommentEntity;
import com.ssafy.dokidog2.map.entity.MarkerEntity;
import com.ssafy.dokidog2.map.repository.MarkerCommentRepository;
import com.ssafy.dokidog2.map.repository.MarkerRepository;
import com.ssafy.dokidog2.user.entity.User;
import com.ssafy.dokidog2.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MarkerCommentService {

    private final MarkerCommentRepository markerCommentRepository;
    private final MarkerRepository markerRepository;
    private final UserRepository userRepository;

    public Long save(MarkerCommentDTO markerCommentDTO) {
        User user = userRepository.findById(markerCommentDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 게시글 ID(markerId)를 검사합니다.
        if (markerCommentDTO.getMarkerId() == null) {
            return null; // 게시글 ID가 없으면 null을 반환합니다.
        }
        /* 부모엔티티(MarkerEntity) 조회 */
        // 게시글 ID로 게시글 엔티티 조회
        Optional<MarkerEntity> optionalMarkerEntity = markerRepository.findById(
                markerCommentDTO.getMarkerId());
        if (optionalMarkerEntity.isPresent()) {
            MarkerEntity markerEntity = optionalMarkerEntity.get();
            // MarkerCommentEntity를 생성하여 댓글 정보를 저장합니다.
            MarkerCommentEntity markerCommentEntity = MarkerCommentEntity.toSaveMarkerCommentEntity(
                    markerCommentDTO, markerEntity, user);
            markerCommentEntity.setMarkerCommentWriter(user.getNickname());
            return markerCommentRepository.save(markerCommentEntity).getMarkerCommentId();
        } else {
            return null; // 게시글 존재하지 않으면 null 반환
        }
    }

    public List<MarkerCommentDTO> findAll(Long markerId) {
        // 게시글 ID로 게시글 엔티티를 조회합니다.
        MarkerEntity markerEntity = markerRepository.findById(markerId).get();
        // 해당 게시글에 대한 모든 댓글을 조회합니다.
        List<MarkerCommentEntity> markerCommentEntityList = markerCommentRepository.findAllByMarkerEntityOrderByMarkerCommentIdAsc(
                markerEntity);
        // 댓글 엔티티 리스트를 DTO 리스트로 변환합니다.
        List<MarkerCommentDTO> markerCommentDTOList = new ArrayList<>();
        for (MarkerCommentEntity markerCommentEntity : markerCommentEntityList) {
            MarkerCommentDTO markerCommentDTO = MarkerCommentDTO.toMarkerCommentDTO(
                    markerCommentEntity, markerId);
            markerCommentDTOList.add(markerCommentDTO);
        }
        return markerCommentDTOList;
    }

    public void delete(Long markerId, Long markerCommentId) {
        // 여기서 markerId를 사용하여 추가적인 검증 로직을 구현할 수 있음
        markerCommentRepository.deleteById(markerCommentId);
    }


    public boolean update(Long markerCommentId, MarkerCommentDTO markerCommentDTO) {
        return markerCommentRepository.findById(markerCommentId).map(markerComment -> {
            markerComment.setMarkerCommentContents(markerCommentDTO.getMarkerCommentContents());
            markerCommentRepository.save(markerComment);
            return true; // 수정 성공
        }).orElse(false); // 해당 ID의 댓글이 없는 경우
    }


}
