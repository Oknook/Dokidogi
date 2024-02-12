package com.ssafy.dokidog2.map.controller;


import com.ssafy.dokidog2.map.dto.MarkerCommentDTO;
import com.ssafy.dokidog2.map.service.MarkerCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/marker")
public class MarkerCommentController {

    private final MarkerCommentService markerCommentService;

    // 댓글 목록 조회
    @GetMapping("/{markerId}/markerComment")
    public ResponseEntity<List<MarkerCommentDTO>> findAll(@PathVariable Long markerId) {
        List<MarkerCommentDTO> markerComments = markerCommentService.findAll(markerId);
        return ResponseEntity.ok(markerComments);
    }

    // 댓글 작성
    @PostMapping("/{markerId}/markerComment/save")
    public ResponseEntity<?> save(
            @RequestBody MarkerCommentDTO markerCommentDTO,
            @PathVariable("markerId") Long markerId) {
        System.out.println("주는 markerCommentDTO = " + markerCommentDTO);
        markerCommentDTO.setMarkerId(markerId); // 여기에서 markerId를 설정
        Long saveResult = markerCommentService.save(markerCommentDTO);
        System.out.println("받는 markerCommentDTO = " + markerCommentDTO);
        if (saveResult != null) {
            List<MarkerCommentDTO> markerCommentDTOList = markerCommentService.findAll(markerId);
            return new ResponseEntity<>(markerCommentDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 마커가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }

    // 댓글 삭제
    @DeleteMapping("/{markerId}/markerComment/{markerCommentId}/delete")
    public ResponseEntity<?> delete(@PathVariable Long markerId,
                                    @PathVariable Long markerCommentId) {
        markerCommentService.delete(markerId, markerCommentId);
        return ResponseEntity.ok().build(); // 삭제 성공 응답
    }


    // 댓글 수정
    @PutMapping("/{markerId}/markerComment/{markerCommentId}/update")
    public ResponseEntity<?> update(@PathVariable Long markerId,
                                    @PathVariable Long markerCommentId,
                                    @RequestBody MarkerCommentDTO markerCommentDTO) {
        markerCommentDTO.setMarkerId(markerId); // 댓글과 연관된 마커 ID 설정
        boolean updateResult = markerCommentService.update(markerCommentId, markerCommentDTO);
        if (updateResult) {
            return ResponseEntity.ok().build(); // 수정 성공 응답
        } else {
            return new ResponseEntity<>("댓글 수정에 실패하였습니다.", HttpStatus.NOT_FOUND);
        }
    }


}
