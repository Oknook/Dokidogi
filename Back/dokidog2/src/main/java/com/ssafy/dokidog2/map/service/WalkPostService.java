package com.ssafy.dokidog2.map.service;



import com.ssafy.dokidog2.map.dto.WalkPostDTO;
import com.ssafy.dokidog2.map.entity.MarkerEntity;
import com.ssafy.dokidog2.map.entity.WalkPostEntity;
import com.ssafy.dokidog2.map.repository.MarkerRepository;
import com.ssafy.dokidog2.map.repository.WalkPostRepository;
import com.ssafy.dokidog2.user.entity.User;
import com.ssafy.dokidog2.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalkPostService {

    private final WalkPostRepository walkPostRepository;
    private final MarkerRepository markerRepository;
    private final UserRepository userRepository;

    public Long save(WalkPostDTO walkPostDTO) {
        User user = userRepository.findById(walkPostDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 게시글 ID(markerId)를 검사합니다.
        if (walkPostDTO.getMarkerId() == null) {
            return null; // 게시글 ID가 없으면 null을 반환합니다.
        }
        /* 부모엔티티(MarkerEntity) 조회 */
        // 게시글 ID로 게시글 엔티티 조회
        Optional<MarkerEntity> optionalMarkerEntity = markerRepository.findById(
                walkPostDTO.getMarkerId());
        if (optionalMarkerEntity.isPresent()) {
            MarkerEntity markerEntity = optionalMarkerEntity.get();
            // WalkPostEntity를 생성하여 댓글 정보를 저장합니다.
            WalkPostEntity walkPostEntity = WalkPostEntity.toSaveWalkPostEntity(walkPostDTO,
                    markerEntity, user);
            walkPostEntity.setWalkPostWriter(user.getNickname());
            return walkPostRepository.save(walkPostEntity).getWalkPostId();
        } else {
            return null; // 게시글 존재하지 않으면 null 반환
        }
    }

    public List<WalkPostDTO> findAll(Long markerId) {
        List<WalkPostEntity> walkPostEntityList = walkPostRepository.findAllByMarkerEntity_MarkerIdOrderByWalkPostIdAsc(markerId);
        List<WalkPostDTO> walkPostDTOList = new ArrayList<>();
        for (WalkPostEntity walkPostEntity : walkPostEntityList) {
            WalkPostDTO walkPostDTO = WalkPostDTO.toWalkPostDTO(walkPostEntity); // 변경된 부분
            walkPostDTOList.add(walkPostDTO);
        }
        return walkPostDTOList;
    }

    public void delete(Long markerId, Long walkPostId) {
        // 여기서 markerId를 사용하여 추가적인 검증 로직을 구현할 수 있음
        walkPostRepository.deleteById(walkPostId);
    }


    public Boolean update(Long walkPostId, WalkPostDTO walkPostDTO) {
        return walkPostRepository.findById(walkPostId).map(walkPost -> {
            walkPost.setWalkPostTitle(walkPostDTO.getWalkPostTitle());
            walkPost.setWalkPostDate(walkPostDTO.getWalkPostDate());
            walkPost.setWalkPostTime(walkPostDTO.getWalkPostTime());
            walkPost.setWalkStart(walkPostDTO.getWalkStart());
            walkPost.setWalkEnd(walkPostDTO.getWalkEnd());
            // 추가적으로 업데이트할 필드가 있다면 여기에 로직 추가
            walkPostRepository.save(walkPost);
            return true; // 성공적으로 업데이트됨
        }).orElse(false); // 게시글을 찾지 못한 경우
    }


}
