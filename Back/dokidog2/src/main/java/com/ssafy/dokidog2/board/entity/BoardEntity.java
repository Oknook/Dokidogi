package com.ssafy.dokidog2.board.entity;


import com.ssafy.dokidog2.board.dto.BoardDTO;
import com.ssafy.dokidog2.user.entity.User;
import com.ssafy.dokidog2.user.repository.UserRepository;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "board_table")
public class BoardEntity extends BaseEntity {

    @Id // pk 컬럼 지정. 필수
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long boardId;

    @Enumerated(EnumType.STRING)
    private BoardCategory boardCategory;

    @Column
    private String title;

    @Column(length = 500)
    private String contents;

    @Column
    private int boardHits;

    @Column
    private int fileAttached; // 1 or 0

    @Column
    private int likes; // 추천 수

    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardFileEntity> boardFileEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CommentEntity> commentEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardLikeEntity> boardLikeEntitiyList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static BoardEntity toSaveEntity(BoardDTO boardDTO, User user) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setUser(user);
        boardEntity.setTitle(boardDTO.getTitle());
        boardEntity.setContents(boardDTO.getContents());
        boardEntity.setBoardHits(0);
        boardEntity.setFileAttached(0); // 파일 없음.
        boardEntity.setBoardCategory(boardDTO.getBoardCategory());
        boardEntity.setLikes(boardDTO.getLikes());
        return boardEntity;
    }

    public static BoardEntity toUpdateEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardId(boardDTO.getBoardId());
        boardEntity.setTitle(boardDTO.getTitle());
        boardEntity.setContents(boardDTO.getContents());
        boardEntity.setBoardHits(boardDTO.getBoardHits());
        boardEntity.setFileAttached(0); // 파일 없음.
        boardEntity.setBoardCategory(boardDTO.getBoardCategory());
        boardEntity.setLikes(boardDTO.getLikes());
        return boardEntity;
    }

    public static BoardEntity toSaveFileEntity(BoardDTO boardDTO , User user) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setUser(user);
        boardEntity.setTitle(boardDTO.getTitle());
        boardEntity.setContents(boardDTO.getContents());
        boardEntity.setBoardCategory(boardDTO.getBoardCategory());
        boardEntity.setBoardHits(0);
        boardEntity.setFileAttached(1); // 파일 있음.
        return boardEntity;
    }
}
