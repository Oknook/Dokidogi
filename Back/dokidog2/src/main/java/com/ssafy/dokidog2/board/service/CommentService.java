package com.ssafy.dokidog2.board.service;


import com.ssafy.dokidog2.board.dto.CommentDTO;
import com.ssafy.dokidog2.board.entity.BoardEntity;
import com.ssafy.dokidog2.board.entity.CommentEntity;
import com.ssafy.dokidog2.board.repository.BoardRepository;
import com.ssafy.dokidog2.board.repository.CommentRepository;
import com.ssafy.dokidog2.user.entity.User;
import com.ssafy.dokidog2.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public Long save(CommentDTO commentDTO) {
        User user = userRepository.findById(commentDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 게시글 ID(boardId)를 검사합니다.
        if (commentDTO.getBoardId() == null) {
            return null; // 게시글 ID가 없으면 null을 반환합니다.
        }
        /* 부모엔티티(BoardEntity) 조회 */
        // 게시글 ID로 게시글 엔티티 조회
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(
            commentDTO.getBoardId());
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            // CommentEntity를 생성하여 댓글 정보를 저장합니다.
            CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDTO, boardEntity, user);
//            log.info("asd",boardEntity);
            System.out.println(boardEntity);
            commentEntity.setCommentWriter(user.getNickname());
            System.out.println("저장전" + commentEntity);
            return commentRepository.save(commentEntity).getCommentId();
        } else {
            return null; // 게시글 존재하지 않으면 null 반환
        }
    }

    public List<CommentDTO> findAll(Long boardId) {
        // 게시글 ID로 게시글 엔티티를 조회합니다.
        BoardEntity boardEntity = boardRepository.findById(boardId).get();
        // 해당 게시글에 대한 모든 댓글을 조회합니다.
        List<CommentEntity> commentEntityList = commentRepository.findAllByBoardEntityOrderByCommentIdAsc(
            boardEntity);
        // 댓글 엔티티 리스트를 DTO 리스트로 변환합니다.
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (CommentEntity commentEntity : commentEntityList) {
            CommentDTO commentDTO = CommentDTO.toCommentDTO(commentEntity, boardId);
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }

    public void delete(Long boardId, Long commentId) {
        // 여기서 boardId를 사용하여 추가적인 검증 로직을 구현할 수 있음
        commentRepository.deleteById(commentId);
    }
}
