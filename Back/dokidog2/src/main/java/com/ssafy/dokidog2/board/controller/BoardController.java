package com.ssafy.dokidog2.board.controller;


import com.ssafy.dokidog2.board.dto.BoardDTO;
import com.ssafy.dokidog2.board.dto.CategoryDTO;
import com.ssafy.dokidog2.board.service.BoardService;
import com.ssafy.dokidog2.board.service.CommentService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
@Slf4j

public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;

    //    // 게시글 저장
    @PostMapping("/save")
    public ResponseEntity<?> save(@ModelAttribute BoardDTO boardDTO) throws IOException {
        BoardDTO savedBoardDTO = boardService.save(boardDTO);
        return ResponseEntity.ok(savedBoardDTO);
    }

    // 게시글 전체 목록 조회
    @GetMapping("/")
    public ResponseEntity<List<BoardDTO>> findAll() {
        List<BoardDTO> boardDTOList = boardService.findAll();
        return ResponseEntity.ok(boardDTOList);
    }

    // 게시글 상세 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDTO> findById(@PathVariable Long boardId) {
        BoardDTO boardDTO = boardService.findById(boardId);
        System.out.println(boardDTO);

        return ResponseEntity.ok(boardDTO); // JSON 형태로 반환
    }

    // 게시글 수정
    @PostMapping("/{boardId}/update")
    public ResponseEntity<?> update(@PathVariable Long boardId, @ModelAttribute BoardDTO boardDTO) {
        try {
            BoardDTO updatedBoard = boardService.update(boardDTO,
                boardId); // 수정된 BoardService.update 메서드 호출 변경
            return ResponseEntity.ok(updatedBoard);
        } catch (IOException e) {
            log.error("File upload error during board update: ", e);
            return ResponseEntity.internalServerError()
                .body("File upload error during board update");
        }
    }

    // 게시글 삭제
    @PostMapping("/{boardId}/delete")
    public ResponseEntity<?> delete(@PathVariable Long boardId) {
        boardService.delete(boardId);
        return ResponseEntity.ok().build(); // 삭제 성공 응답
    }

    //좋아요
    @PostMapping("/{boardId}/like")
    public ResponseEntity<?> likeBoard(@PathVariable Long boardId) {
        boardService.likeBoard(boardId);
        return ResponseEntity.ok().build();
    }

    // 카테고리별 게시글 리스트 검색 및 반환
    @PostMapping("/category")
    public ResponseEntity<List<BoardDTO>> findByCategory(@RequestBody CategoryDTO categoryDTO) {
        System.out.println("받는 카테고리");
        System.out.println(categoryDTO);
        List<BoardDTO> boardDTOs = boardService.findByCategory(categoryDTO.getBoardCategory());
        if (boardDTOs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        System.out.println("다시 보내는");
        System.out.println(boardDTOs);
        return ResponseEntity.ok(boardDTOs);
    }
}