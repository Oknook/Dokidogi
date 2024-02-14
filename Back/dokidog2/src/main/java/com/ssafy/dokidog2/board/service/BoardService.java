package com.ssafy.dokidog2.board.service;

// DTO -> Entity (Entity Class)
// Entity -> DTO (DTO Class)


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ssafy.dokidog2.board.dto.BoardDTO;
import com.ssafy.dokidog2.board.entity.BoardCategory;
import com.ssafy.dokidog2.board.entity.BoardEntity;
import com.ssafy.dokidog2.board.entity.BoardFileEntity;
import com.ssafy.dokidog2.board.repository.BoardFileRepository;
import com.ssafy.dokidog2.board.repository.BoardLikeRepository;
import com.ssafy.dokidog2.board.repository.BoardRepository;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final BoardLikeRepository boardLikeRepository;

    private final AmazonS3 amazonS3Client;

    private String bucketName = "donghotest";

    public String uploadFileToS3(MultipartFile file, String storedFileName) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        amazonS3Client.putObject(new PutObjectRequest(bucketName, storedFileName, file.getInputStream(), metadata)
            .withCannedAcl(CannedAccessControlList.PublicRead)); // 파일을 public-read 권한으로 업로드

        return amazonS3Client.getUrl(bucketName, storedFileName).toString(); // 업로드된 파일의 URL 반환
    }

    public BoardDTO save(BoardDTO boardDTO) throws IOException {
        BoardEntity savedBoardEntity;
        if (boardDTO.getBoardFile() != null && !boardDTO.getBoardFile().isEmpty()) {
            // 파일 첨부가 있는 경우의 로직
            MultipartFile boardFile = boardDTO.getBoardFile();
            String originalFilename = boardFile.getOriginalFilename();
            String storedFileName = System.currentTimeMillis() + "_" + originalFilename;

            // S3에 파일 업로드하고 URL 반환
            String imgUrl = uploadFileToS3(boardFile, storedFileName);

            // BoardDTO에 파일 정보와 URL 설정
            boardDTO.setOriginalFileName(originalFilename);
            boardDTO.setStoredFileName(storedFileName);
            boardDTO.setImgUrl(imgUrl); // S3 URL 설정

            // 엔티티 저장 로직
            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO);
            Long savedId = boardRepository.save(boardEntity).getBoardId();
            savedBoardEntity = boardRepository.findById(savedId).get();

            // 파일 정보 엔티티 생성 및 저장
            BoardFileEntity boardFileEntity = new BoardFileEntity();
            boardFileEntity.setOriginalFileName(originalFilename);
            boardFileEntity.setStoredFileName(storedFileName);
            boardFileEntity.setImgUrl(imgUrl);
            boardFileEntity.setBoardEntity(savedBoardEntity);
            boardFileRepository.save(boardFileEntity);
        } else {
            // 첨부 파일 없음
            BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
            savedBoardEntity = boardRepository.save(boardEntity);
        }
        boardDTO.setBoardId(savedBoardEntity.getBoardId());
        return BoardDTO.toBoardDTO(savedBoardEntity); // 업데이트된 BoardDTO 반환
    }


    @Transactional
    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (BoardEntity boardEntity : boardEntityList) {
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        return boardDTOList;
    }

    @Transactional
    public void updateHits(Long boardId) {
        boardRepository.updateHits(boardId);
    }

    @Transactional
    public BoardDTO findById(Long boardId) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(boardId);
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
            return boardDTO;
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

    @Transactional
    public BoardDTO update(BoardDTO boardDTO, Long boardId) throws IOException {
        BoardEntity existingBoard = boardRepository.findById(boardId)
            .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + boardId));

        // 새 이미지 파일이 있을 경우 처리
        if (boardDTO.getBoardFile() != null && !boardDTO.getBoardFile().isEmpty()) {
            // 기존 이미지 파일 처리 (S3에서 삭제)
            existingBoard.getBoardFileEntityList().forEach(fileEntity -> {
                deleteFileFromS3(fileEntity.getStoredFileName()); // S3에서 파일 삭제
                boardFileRepository.delete(fileEntity); // DB에서 파일 정보 삭제
            });
            existingBoard.getBoardFileEntityList().clear(); // 엔티티 내 파일 리스트 클리어

            MultipartFile newImageFile = boardDTO.getBoardFile();
            String originalFilename = newImageFile.getOriginalFilename();
            String storedFileName = System.currentTimeMillis() + "_" + originalFilename;

            // S3에 파일 업로드
            String imgUrl = uploadFileToS3(newImageFile, storedFileName);

            // 파일 정보 엔티티 생성 및 저장
            BoardFileEntity newFileEntity = new BoardFileEntity();
            newFileEntity.setOriginalFileName(originalFilename);
            newFileEntity.setStoredFileName(storedFileName);
            newFileEntity.setImgUrl(imgUrl); // S3 URL 설정
            newFileEntity.setBoardEntity(existingBoard);
            boardFileRepository.save(newFileEntity);
        }

        // 게시글 정보 업데이트
        existingBoard.setTitle(boardDTO.getTitle());
        existingBoard.setContents(boardDTO.getContents());
        // 필요한 추가 정보 업데이트

        boardRepository.save(existingBoard); // 게시글 엔티티 저장

        return BoardDTO.toBoardDTO(existingBoard); // 업데이트된 게시글 정보를 DTO로 변환하여 반환
    }

    public void delete(Long boardId) {
        boardRepository.deleteById(boardId);
    }

    public List<BoardDTO> findByCategory(String category) {
        BoardCategory boardCategory = BoardCategory.of(category);
        if (boardCategory == null) {
            // 카테고리가 enum 값과 일치하지 않는 경우 처리
            // 예를 들어, 빈 리스트를 반환하거나 예외를 발생시킴
            return new ArrayList<>();
        }
        List<BoardEntity> boardEntities = boardRepository.findByBoardCategory(boardCategory);
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (BoardEntity boardEntity : boardEntities) {
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        return boardDTOList;
    }


    public List<BoardDTO> findAllPosts() {
        List<BoardEntity> boardEntities = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (BoardEntity boardEntity : boardEntities) {
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        return boardDTOList;
    }

    @Transactional
    public void likeBoard(Long boardId) {
        BoardEntity board = boardRepository.findById(boardId)
            .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. ID: " + boardId));
        board.setLikes(board.getLikes() + 1);
        boardRepository.save(board);
    }

}