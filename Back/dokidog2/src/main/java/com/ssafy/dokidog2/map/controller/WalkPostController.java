package com.ssafy.dokidog2.map.controller;


import com.ssafy.dokidog2.map.dto.WalkPostDTO;
import com.ssafy.dokidog2.map.service.WalkPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/walkPost")
public class WalkPostController {

    private final WalkPostService walkPostService;

    // 산책 목록 조회
    @GetMapping("/{markerId}")
    public ResponseEntity<List<WalkPostDTO>> findAll(@PathVariable Long markerId) {
        List<WalkPostDTO> walkPosts = walkPostService.findAll(markerId);
        return ResponseEntity.ok(walkPosts);
    }

    // 산책 작성
    @PostMapping("/{markerId}/save")
    public ResponseEntity<?> save(
            @RequestBody WalkPostDTO walkPostDTO,
            @PathVariable("markerId") Long markerId) {
        System.out.println("walkPostDTO = " + walkPostDTO);
        walkPostDTO.setMarkerId(markerId); // 여기에서 markerId를 설정
        Long saveResult = walkPostService.save(walkPostDTO);
        if (saveResult != null) {
            List<WalkPostDTO> walkPostDTOList = walkPostService.findAll(markerId);
            return new ResponseEntity<>(walkPostDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }

    // 산책 삭제
    @DeleteMapping("/{markerId}/{walkPostId}/delete")
    public ResponseEntity<?> delete(@PathVariable Long markerId, @PathVariable Long walkPostId) {
        walkPostService.delete(markerId, walkPostId);
        return ResponseEntity.ok().build(); // 삭제 성공 응답
    }


    // 산책 수정
    @PutMapping("/{markerId}/{walkPostId}/update")
    public ResponseEntity<?> update(
            @PathVariable Long markerId,
            @PathVariable Long walkPostId,
            @RequestBody WalkPostDTO walkPostDTO) {
        walkPostDTO.setMarkerId(markerId); // Marker ID 설정
        Boolean updateResult = walkPostService.update(walkPostId, walkPostDTO);
        if (updateResult) {
            return new ResponseEntity<>("게시글이 성공적으로 업데이트되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("게시글 업데이트를 실패하였습니다.", HttpStatus.NOT_FOUND);
        }
    }


}
