package com.ssafy.dokidog2.board.service;

// DTO -> Entity (Entity Class)
// Entity -> DTO (DTO Class)


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

    // 이미지 파일을 저장할 기본 경로
//    private final String imageDirectoryPath = "C:/Users/zxcas/imgtest/";
    private final String imageDirectoryPath = "C:/Users/SSAFY/imgtest/";

    public BoardDTO save(BoardDTO boardDTO) throws IOException {
        BoardEntity savedBoardEntity;
        if (boardDTO.getBoardFile() != null && !boardDTO.getBoardFile().isEmpty()) {
            // 파일 첨부가 있는 경우의 로직
            MultipartFile boardFile = boardDTO.getBoardFile(); // 1. DTO에 담긴 파일을 꺼냄
            String originalFilename = boardFile.getOriginalFilename(); // 2. 파일의 이름 가져옴
            String storedFileName =
                System.currentTimeMillis() + "_" + originalFilename; // 3. 서버 저장용 이름을 만듦

            // 이미 선언된 imageDirectoryPath를 사용하여 저장 경로 설정
            String imgUrl = "/images/" + storedFileName; // 이미지 URL 생성
            File directory = new File(imageDirectoryPath);
            if (!directory.exists()) {
                directory.mkdirs(); // 디렉토리가 존재하지 않으면 생성
            }
            File targetFile = new File(imageDirectoryPath + storedFileName); // 파일 저장 경로
            try {
                boardFile.transferTo(targetFile); // 5. 해당 경로에 파일 저장
            } catch (IOException e) {
                e.printStackTrace(); // 오류 처리 로직
            }

            // BoardDTO에 파일 이름 설정
            boardDTO.setOriginalFileName(originalFilename);
            boardDTO.setStoredFileName(storedFileName);
            boardDTO.setImgUrl(imgUrl);

            // 엔티티 저장 로직
            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO);
            Long savedId = boardRepository.save(boardEntity).getBoardId();
            savedBoardEntity = boardRepository.findById(savedId).get();

            BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(savedBoardEntity,
                originalFilename, storedFileName, imgUrl);
            boardFileRepository.save(boardFileEntity);
        } else {
            // 첨부 파일 없음
            BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
            savedBoardEntity = boardRepository.save(boardEntity);
        }
        boardDTO.setBoardId(savedBoardEntity.getBoardId());
        return boardDTO; // 업데이트된 BoardDTO 반환
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

    @Transactional
    public BoardDTO update(BoardDTO boardDTO, Long boardId) throws IOException {
        // 게시글 정보 조회
        BoardEntity existingBoard = boardRepository.findById(boardId)
            .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + boardId));

        // 기존 이미지 파일 삭제
        if (!existingBoard.getBoardFileEntityList().isEmpty()) {
            for (BoardFileEntity fileEntity : existingBoard.getBoardFileEntityList()) {
                Path filePath = Paths.get(imageDirectoryPath + fileEntity.getStoredFileName());
                Files.deleteIfExists(filePath); // 파일 시스템에서 파일 삭제
                boardFileRepository.delete(fileEntity); // DB에서 파일 정보 삭제
            }
            existingBoard.getBoardFileEntityList().clear(); // 엔티티 내 파일 리스트 클리어
        }

        // 새 이미지 파일 저장
        MultipartFile newImageFile = boardDTO.getBoardFile();
        if (newImageFile != null && !newImageFile.isEmpty()) {
            String originalFilename = newImageFile.getOriginalFilename();
            String storedFileName = System.currentTimeMillis() + "_" + originalFilename;
            Path storagePath = Paths.get(imageDirectoryPath + storedFileName);
            Files.copy(newImageFile.getInputStream(), storagePath); // 파일 시스템에 파일 저장

            // 파일 정보 엔티티 생성 및 저장
            BoardFileEntity newFileEntity = new BoardFileEntity();
            newFileEntity.setOriginalFileName(originalFilename);
            newFileEntity.setStoredFileName(storedFileName);
            newFileEntity.setImgUrl("/images/" + storedFileName);
            newFileEntity.setBoardEntity(existingBoard);
            boardFileRepository.save(newFileEntity);

            existingBoard.getBoardFileEntityList().add(newFileEntity); // 게시글 엔티티에 파일 정보 추가
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

    //    public Page<BoardDTO> paging(Pageable pageable) {
//        int page = pageable.getPageNumber() - 1;
//        int pageLimit = 3; // 한 페이지에 보여줄 글 갯수
//        // 한페이지당 3개씩 글을 보여주고 정렬 기준은 id 기준으로 내림차순 정렬
//        // page 위치에 있는 값은 0부터 시작
//        Page<BoardEntity> boardEntities =
//                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.ASC, "boardId")));
//        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
//        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
//        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
//        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
//        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
//        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
//        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
//        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부
//        // 목록: id, writer, title, hits, createdTime, likes, category
//        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(board.getBoardId(), board.getBoardWriter(), board.getTitle(), board.getBoardHits(), board.getCreatedTime(), board.getLikes(), board.getBoardCategory()));
//        return boardDTOS;
//    }
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

//    private File convert(MultipartFile file) {
//        File convFile = null;
//        boolean result = true;
//        try {
//            // 혹시나 파일 이름이 중복될 수 있으니 파일 이름 앞에 랜덤한 숫자값을 덧붙여줌
//            convFile = new File(tmpLocation + "/" + Math.abs(LocalDateTime.now().hashCode()) + "_" + URLDecoder.decode(file.getOriginalFilename(), "UTF-8"));
//            convFile.createNewFile();
//            FileOutputStream fos = new FileOutputStream(convFile);
//            fos.write(file.getBytes());
//            fos.close();
//            return convFile;
//
//        } catch (Exception e){
//            result = false;
//            e.printStackTrace();
//
//        } finally {
//            if(result=false && convFile.exists()){
//                convFile.delete();
//            }
//        }
//        return null;
//    }
}