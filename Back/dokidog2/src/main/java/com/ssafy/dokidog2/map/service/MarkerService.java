package com.ssafy.dokidog2.map.service;


import com.ssafy.dokidog2.map.dto.MarkerDTO;
import com.ssafy.dokidog2.map.entity.MarkerEntity;
import com.ssafy.dokidog2.map.entity.MarkerFileEntity;
import com.ssafy.dokidog2.map.repository.MarkerFileRepository;
import com.ssafy.dokidog2.map.repository.MarkerRepository;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ssafy.dokidog2.user.entity.User;
import com.ssafy.dokidog2.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MarkerService {

    private final MarkerRepository markerRepository;
    private final MarkerFileRepository markerFileRepository;
    private final UserRepository userRepository;

    public MarkerDTO markerSave(MarkerDTO markerDTO) throws IOException {
        MarkerEntity savedMarkerEntity; // Declare a variable to hold the saved entity
        User user = userRepository.findById(markerDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (markerDTO.getMarkerBoardFile() != null && !markerDTO.getMarkerBoardFile().isEmpty()) {
            // If there is a file attachment
            MultipartFile markerBoardFile = markerDTO.getMarkerBoardFile();

            String markerOriginalFileName = markerBoardFile.getOriginalFilename();
            String markerStoredFileName = System.currentTimeMillis() + "_" + markerOriginalFileName;
            String directoryPath = "C:/Users/SSAFY/imgtest/"; // Directory path to save
//            String directoryPath = "C:/Users/zxcas/imgtest/"; // Directory path to save
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs(); // Create directory if it does not exist
            }
            File targetFile = new File(directoryPath + markerStoredFileName);
            try {
                markerBoardFile.transferTo(targetFile); // save file
            } catch (IOException e) {
                e.printStackTrace();
                // Error handling logic (logging exceptions, notifying users of errors, etc.)
            }

            // set dto file name
            markerDTO.setMarkerOriginalFileName(markerOriginalFileName);
            markerDTO.setMarkerStoredFileName(markerStoredFileName);

            // Save entity with file
            MarkerEntity markerEntity = MarkerEntity.toSaveFileMarkerEntity(markerDTO, user);
            markerEntity.setMarkerWriter(user.getNickname());
            savedMarkerEntity = markerRepository.save(markerEntity); // Save and get saved entity

            MarkerFileEntity markerFileEntity = MarkerFileEntity.toMarkerFileEntity(
                savedMarkerEntity, markerOriginalFileName, markerStoredFileName);
            markerFileRepository.save(markerFileEntity);
        } else {
            // No attachments
            MarkerEntity markerEntity = MarkerEntity.toSaveMarkerEntity(markerDTO, user);
            markerEntity.setMarkerWriter(user.getNickname());
            savedMarkerEntity = markerRepository.save(markerEntity); // Save and get saved entity
        }

        // After saving, update the DTO with the generated ID
        markerDTO.setMarkerId(
            savedMarkerEntity.getMarkerId()); // Set the saved marker's ID back to DTO
        markerDTO.setMarkerWriter(user.getNickname());
        return markerDTO; // Return the updated DTO
    }

    @Transactional
    public List<MarkerDTO> findAll() {
        List<MarkerEntity> markerEntityList = markerRepository.findAll();
        List<MarkerDTO> markerDTOList = new ArrayList<>();
        for (MarkerEntity markerEntity : markerEntityList) {
            markerDTOList.add(MarkerDTO.toMarkerDTO(markerEntity));
        }
        return markerDTOList;
    }

    @Transactional
    public MarkerDTO findById(Long markerId) {
        Optional<MarkerEntity> optionalMarkerEntity = markerRepository.findById(markerId);
        if (optionalMarkerEntity.isPresent()) {
            MarkerEntity markerEntity = optionalMarkerEntity.get();
            MarkerDTO markerDTO = MarkerDTO.toMarkerDTO(markerEntity);
            return markerDTO;
        } else {
            return null;
        }
    }

    // MarkerService.java에 마커 업데이트 로직 추가
    public MarkerDTO updateMarker(Long markerId, MarkerDTO markerDTO, MultipartFile file)
        throws IOException {
        MarkerEntity markerEntity = markerRepository.findById(markerId)
            .orElseThrow(
                () -> new IllegalArgumentException("Marker not found with ID: " + markerId));

        // 기존 파일 처리 로직 (기존 파일이 있다면 삭제)
        if (!markerEntity.getMarkerFileEntityList().isEmpty()) {
            MarkerFileEntity existingFile = markerEntity.getMarkerFileEntityList()
                .get(0); // 예시는 단일 파일을 가정
            File oldFile = new File("your_storage_path" + existingFile.getMarkerStoredFileName());
            if (oldFile.exists()) {
                oldFile.delete(); // 실제 파일 삭제
                markerFileRepository.delete(existingFile); // DB에서 파일 엔티티 삭제
                markerEntity.getMarkerFileEntityList().clear(); // 엔티티 리스트에서 제거
            }
        }

        // 새 파일 처리
        if (file != null && !file.isEmpty()) {
            String originalFileName = file.getOriginalFilename();
            String storedFileName = System.currentTimeMillis() + "_" + originalFileName;
            File targetFile = new File("your_storage_path" + storedFileName);
            file.transferTo(targetFile);

            // 파일 엔티티 생성 및 저장
            MarkerFileEntity newFileEntity = new MarkerFileEntity();
            newFileEntity.setMarkerOriginalFileName(originalFileName);
            newFileEntity.setMarkerStoredFileName(storedFileName);
            newFileEntity.setMarkerEntity(markerEntity); // 관계 설정
            markerFileRepository.save(newFileEntity);

            // 엔티티 리스트에 추가
            markerEntity.getMarkerFileEntityList().add(newFileEntity);
            markerEntity.setMarkerFileAttached(1); // 파일 첨부 여부 업데이트
        } else {
            markerEntity.setMarkerFileAttached(0); // 파일 미첨부 상태로 업데이트
        }

        // 마커 정보 업데이트
        markerEntity.setMarkerTitle(markerDTO.getMarkerTitle());
        markerEntity.setMarkerContents(markerDTO.getMarkerContents());
        // 필요한 추가 정보 업데이트

        markerRepository.save(markerEntity);

        return MarkerDTO.toMarkerDTO(markerEntity);
    }


    public void markerDelete(Long markerId) {
        markerRepository.deleteById(markerId);
    }

    @Transactional
    public void likeMarker(Long markerId) {
        MarkerEntity markerEntity = markerRepository.findById(markerId)
            .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. ID: " + markerId));
        markerEntity.setMarkerLikes(markerEntity.getMarkerLikes() + 1);
        markerRepository.save(markerEntity);
    }
}