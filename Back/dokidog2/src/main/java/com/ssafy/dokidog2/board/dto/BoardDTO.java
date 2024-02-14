package com.ssafy.dokidog2.board.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.dokidog2.board.entity.BoardCategory;
import com.ssafy.dokidog2.board.entity.BoardEntity;
import com.ssafy.dokidog2.board.entity.BoardFileEntity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

// DTO(Data Transfer Object), VO, Bean,         Entity
@Getter
@Setter
@ToString
@NoArgsConstructor // 기본생성자
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자
public class BoardDTO {

    private Long boardId;
    private String boardWriter;
    private String boardPass;
    private String title;
    private String contents;
    private int boardHits;
    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdatedTime;

    private int likes; // 추천 수
    @JsonIgnore
    private MultipartFile boardFile; // save.html -> Controller 파일 담는 용도
    private String originalFileName; // 원본 파일 이름
    private String storedFileName; // 서버 저장용 파일 이름
    //    private List<String> originalFileName; // 원본 파일 이름
//    private List<String> storedFileName; // 서버 저장용 파일 이름
    private int fileAttached; // 파일 첨부 여부(첨부 1, 미첨부 0)
    private String imgUrl; // .이미지 URL 필드

    @Enumerated(EnumType.STRING)
    private BoardCategory boardCategory;

    public BoardDTO(Long boardId, String boardWriter, String title, int boardHits,
        LocalDateTime boardCreatedTime, int likes, BoardCategory boardCategory) {
        this.boardId = boardId;
        this.boardWriter = boardWriter;
        this.title = title;
        this.boardHits = boardHits;
        this.boardCreatedTime = boardCreatedTime;
        this.likes = likes;
        this.boardCategory = boardCategory;
//        this.boardFile = boardFile;
    }

    public static BoardDTO toBoardDTO(BoardEntity boardEntity) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBoardId(boardEntity.getBoardId());
        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
        boardDTO.setTitle(boardEntity.getTitle());
        boardDTO.setContents(boardEntity.getContents());
        boardDTO.setBoardHits(boardEntity.getBoardHits());
        boardDTO.setBoardCreatedTime(boardEntity.getCreatedTime());
        boardDTO.setBoardUpdatedTime(boardEntity.getUpdatedTime());
        boardDTO.setLikes(boardEntity.getLikes());
        boardDTO.setBoardCategory(boardEntity.getBoardCategory());

        // 파일 리스트가 비어있지 않은 경우에만 파일 정보 설정
        if (!boardEntity.getBoardFileEntityList().isEmpty()) {
            BoardFileEntity boardFileEntity = boardEntity.getBoardFileEntityList().get(0);
            boardDTO.setOriginalFileName(boardFileEntity.getOriginalFileName());
            boardDTO.setStoredFileName(boardFileEntity.getStoredFileName());
            boardDTO.setImgUrl(boardFileEntity.getImgUrl());
            boardDTO.setFileAttached(1);
        } else {
            boardDTO.setFileAttached(0);
        }

        return boardDTO;
    }
}
