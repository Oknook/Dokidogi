package com.ssafy.dokidog2.map.service;


import com.ssafy.dokidog2.map.dto.MarkerDTO;
import com.ssafy.dokidog2.map.entity.MarkerEntity;
import com.ssafy.dokidog2.map.entity.MarkerFileEntity;
import com.ssafy.dokidog2.map.repository.MarkerFileRepository;
import com.ssafy.dokidog2.map.repository.MarkerRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MarkerService {

    private final MarkerRepository markerRepository;
    private final MarkerFileRepository markerFileRepository;

    private final String imageDirectoryPath = "C:/Users/SSAFY/imgtest/";
//    private final String imageDirectoryPath = "C:/Users/zxcas/imgtest/";

    public MarkerDTO markerSave(MarkerDTO markerDTO) throws IOException {
        MarkerEntity savedMarkerEntity;

        if (markerDTO.getMarkerBoardFile() != null && !markerDTO.getMarkerBoardFile().isEmpty()) {
            // 파일 첨부가 있는 경우
            MultipartFile markerBoardFile = markerDTO.getMarkerBoardFile();
            String markerOriginalFileName = markerBoardFile.getOriginalFilename();
            String markerStoredFileName = System.currentTimeMillis() + "_" + markerOriginalFileName;

            // imageDirectoryPath를 직접 사용하여 파일 저장 경로 설정
            File directory = new File(imageDirectoryPath);
            if (!directory.exists()) {
                directory.mkdirs(); // 디렉토리가 존재하지 않으면 생성
            }
            File targetFile = new File(imageDirectoryPath + markerStoredFileName);
            try {
                markerBoardFile.transferTo(targetFile); // 파일 저장
            } catch (IOException e) {
                e.printStackTrace(); // 오류 처리
            }

            // DTO에 파일 이름 설정
            markerDTO.setMarkerOriginalFileName(markerOriginalFileName);
            markerDTO.setMarkerStoredFileName(markerStoredFileName);

            // 엔티티 저장 로직
            MarkerEntity markerEntity = MarkerEntity.toSaveFileMarkerEntity(markerDTO);
            savedMarkerEntity = markerRepository.save(markerEntity);

            MarkerFileEntity markerFileEntity = MarkerFileEntity.toMarkerFileEntity(
                savedMarkerEntity, markerOriginalFileName, markerStoredFileName);
            markerFileRepository.save(markerFileEntity);
        } else {
            // 첨부 파일이 없는 경우
            MarkerEntity markerEntity = MarkerEntity.toSaveMarkerEntity(markerDTO);
            savedMarkerEntity = markerRepository.save(markerEntity);
        }

        // 저장 후 DTO에 ID 설정
        markerDTO.setMarkerId(savedMarkerEntity.getMarkerId());
        return markerDTO; // 업데이트된 DTO 반환
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

    @Transactional
    public MarkerDTO updateMarker(Long markerId, MarkerDTO markerDTO) throws IOException {
        // 마커 정보 조회
        MarkerEntity existingMarker = markerRepository.findById(markerId)
            .orElseThrow(() -> new IllegalArgumentException("마커를 찾을 수 없습니다. ID: " + markerId));

        // 기존 이미지 파일 삭제
        if (!existingMarker.getMarkerFileEntityList().isEmpty()) {
            for (MarkerFileEntity fileEntity : existingMarker.getMarkerFileEntityList()) {
                Path filePath = Paths.get(
                    imageDirectoryPath + fileEntity.getMarkerStoredFileName());
                Files.deleteIfExists(filePath); // 파일 시스템에서 파일 삭제
                markerFileRepository.delete(fileEntity); // 데이터베이스에서 파일 정보 삭제
            }
            existingMarker.getMarkerFileEntityList().clear(); // 엔티티 내 파일 리스트 클리어
        }

        // 새 이미지 파일 저장
        if (markerDTO.getMarkerBoardFile() != null && !markerDTO.getMarkerBoardFile().isEmpty()) {
            MultipartFile newImageFile = markerDTO.getMarkerBoardFile();
            String originalFilename = newImageFile.getOriginalFilename();
            String storedFileName = System.currentTimeMillis() + "_" + originalFilename;
            Path storagePath = Paths.get(imageDirectoryPath + storedFileName);
            Files.copy(newImageFile.getInputStream(), storagePath,
                StandardCopyOption.REPLACE_EXISTING); // 파일 시스템에 파일 저장

            // 파일 정보 엔티티 생성 및 저장
            MarkerFileEntity newFileEntity = new MarkerFileEntity();
            newFileEntity.setMarkerOriginalFileName(originalFilename);
            newFileEntity.setMarkerStoredFileName(storedFileName);
            newFileEntity.setMarkerEntity(existingMarker);
            markerFileRepository.save(newFileEntity);

            existingMarker.getMarkerFileEntityList().add(newFileEntity); // 마커 엔티티에 파일 정보 추가
        }

        // 마커 정보 업데이트
        existingMarker.setMarkerTitle(markerDTO.getMarkerTitle());
        existingMarker.setMarkerContents(markerDTO.getMarkerContents());
        // 필요한 추가 정보 업데이트

        markerRepository.save(existingMarker); // 마커 엔티티 저장

        return MarkerDTO.toMarkerDTO(existingMarker); // 업데이트된 마커 정보를 DTO로 변환하여 반환
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