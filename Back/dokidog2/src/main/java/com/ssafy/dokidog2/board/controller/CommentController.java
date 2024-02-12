package com.ssafy.dokidog2.board.controller;


import com.ssafy.dokidog2.board.dto.CommentDTO;
import com.ssafy.dokidog2.board.service.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class CommentController {

    private final CommentService commentService;

    // 댓글 목록 조회
    @GetMapping("/{boardId}/comment")
    public ResponseEntity<List<CommentDTO>> findAll(@PathVariable Long boardId) {
        List<CommentDTO> comments = commentService.findAll(boardId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 작성
    @PostMapping("/{boardId}/comment/save")
    public ResponseEntity<?> save(@RequestBody CommentDTO commentDTO,
        @PathVariable("boardId") Long boardId) {
        System.out.println("받는 commentDTO = " + commentDTO);
        commentDTO.setBoardId(boardId); // 여기에서 boardId를 설정
        Long saveResult = commentService.save(commentDTO);
        System.out.println("주는 commentDTO = " + commentDTO);
        if (saveResult != null) {
            List<CommentDTO> commentDTOList = commentService.findAll(boardId);
            return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }

    // 댓글 삭제
    @PostMapping("/{boardId}/comment/{commentId}/delete")
    public ResponseEntity<?> delete(@PathVariable Long boardId, @PathVariable Long commentId) {
        commentService.delete(boardId, commentId);
        System.out.println("+++++++++++++");
        System.out.println(boardId);
        System.out.println(commentId);
        return ResponseEntity.ok().build(); // 삭제 성공 응답
    }

}
