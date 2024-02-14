package com.ssafy.dokidog2.map.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
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

    private final AmazonS3 amazonS3Client;

    private String bucketName = "donghotest";

    public String uploadFileToS3(MultipartFile file, String storedFileName) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        amazonS3Client.putObject(new PutObjectRequest(bucketName, storedFileName, file.getInputStream(), metadata)
            .withCannedAcl(CannedAccessControlList.PublicRead)); // 파일을 public-read 권한으로 업로드

        return amazonS3Client.getUrl(bucketName, storedFileName).toString(); // 업로드된 파일의 URL 반환
    }

    public MarkerDTO markerSave(MarkerDTO markerDTO) throws IOException {
        MarkerEntity savedMarkerEntity; // Declare a variable to hold the saved entity

        if (markerDTO.getMarkerBoardFile() != null && !markerDTO.getMarkerBoardFile().isEmpty()) {
            // If there is a file attachment
            MultipartFile markerBoardFile = markerDTO.getMarkerBoardFile();
            String markerOriginalFileName = markerBoardFile.getOriginalFilename();
            String markerStoredFileName = System.currentTimeMillis() + "_" + markerOriginalFileName;

            // S3에 파일 업로드하고 URL 반환
            String imgUrl = uploadFileToS3(markerBoardFile, markerStoredFileName);

            // 파일 정보와 URL 설정
            markerDTO.setMarkerOriginalFileName(markerOriginalFileName);
            markerDTO.setMarkerStoredFileName(markerStoredFileName);
            markerDTO.setImgUrl(imgUrl);

            // 엔티티 저장 로직
            MarkerEntity markerEntity = MarkerEntity.toSaveFileMarkerEntity(markerDTO);
            savedMarkerEntity = markerRepository.save(markerEntity); // Save and get saved entity

            MarkerFileEntity markerFileEntity = MarkerFileEntity.toMarkerFileEntity(
                savedMarkerEntity, markerOriginalFileName, markerStoredFileName, imgUrl);
            markerFileRepository.save(markerFileEntity);
        } else {
            // 첨부 파일 없음
            MarkerEntity markerEntity = MarkerEntity.toSaveMarkerEntity(markerDTO);
            savedMarkerEntity = markerRepository.save(markerEntity); // Save and get saved entity
        }

        // After saving, update the DTO with the generated ID
        markerDTO.setMarkerId(
            savedMarkerEntity.getMarkerId()); // Set the saved marker's ID back to DTO
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
    // 파일을 S3에서 삭제하는 메서드를 추가합니다.
    public void deleteFileFromS3(String storedFileName) {
        if (amazonS3Client.doesObjectExist(bucketName, storedFileName)) {
            amazonS3Client.deleteObject(bucketName, storedFileName);
        }
    }

    // MarkerService.java에 마커 업데이트 로직 추가
    public MarkerDTO updateMarker(Long markerId, MarkerDTO markerDTO)
        throws IOException {
        MarkerEntity markerEntity = markerRepository.findById(markerId)
            .orElseThrow(
                () -> new IllegalArgumentException("Marker not found with ID: " + markerId));

        // 새 이미지 파일 있는 경우 처리
        if (!markerEntity.getMarkerFileEntityList().isEmpty()) {
            markerEntity.getMarkerFileEntityList().forEach(fileEntity -> {
                deleteFileFromS3(fileEntity.getMarkerStoredFileName()); // S3에서 파일 삭제
                markerFileRepository.delete(fileEntity); // DB에서 파일 정보 삭제
            });
            markerEntity.getMarkerFileEntityList().clear(); // 엔티티 내 파일 리스트 클리어

            MultipartFile newImageFile = markerDTO.getMarkerBoardFile();
            String originalFilename = newImageFile.getOriginalFilename();
            String storedFileName = System.currentTimeMillis() + "_" + originalFilename;

            // S3에 파일 업로드 및 URL 저장
            String imgUrl = uploadFileToS3(newImageFile, storedFileName);

            // 파일 정보 엔티티 생성 및 저장
            MarkerFileEntity newFileEntity = new MarkerFileEntity();
            newFileEntity.setMarkerOriginalFileName(originalFilename);
            newFileEntity.setMarkerStoredFileName(storedFileName);
            newFileEntity.setImgUrl(imgUrl); // S3 URL 설정
            newFileEntity.setMarkerEntity(markerEntity);
            markerFileRepository.save(newFileEntity);
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