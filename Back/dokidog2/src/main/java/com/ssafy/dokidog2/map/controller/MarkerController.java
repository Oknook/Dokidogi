package com.ssafy.dokidog2.map.controller;



import com.ssafy.dokidog2.map.dto.MarkerDTO;
import com.ssafy.dokidog2.map.repository.MarkerRepository;
import com.ssafy.dokidog2.map.service.MarkerService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/api/marker", method = {RequestMethod.GET, RequestMethod.POST,
    RequestMethod.DELETE})
@Slf4j
public class MarkerController {

    private final MarkerService markerService;
    private final MarkerRepository markerRepository;

    //  마커 저장
    @PostMapping("/save")
    public ResponseEntity<?> save(@ModelAttribute MarkerDTO markerDTO) throws IOException {
        MarkerDTO savedMarkerDTO = markerService.markerSave(markerDTO);
        return ResponseEntity.ok(savedMarkerDTO);
    }

    // 마커 전체 리스트
    @GetMapping("/")
    public ResponseEntity<List<MarkerDTO>> findAll() {
        List<MarkerDTO> markerDTOList = markerService.findAll();
        return ResponseEntity.ok(markerDTOList);
    }

    // 마커 상세조회
    @GetMapping("/{markerId}")
    public ResponseEntity<MarkerDTO> findById(@PathVariable Long markerId) {
        MarkerDTO markerDTO = markerService.findById(markerId);
        System.out.println("상세조회");
        System.out.println(markerDTO);
        return ResponseEntity.ok(markerDTO);
    }

    // 마커 삭제
    @DeleteMapping("/{markerId}/delete")
    public ResponseEntity<?> delete(@PathVariable Long markerId) {
        markerService.markerDelete(markerId);
        return ResponseEntity.ok().build();
    }


    // MarkerController.java에 마커 업데이트 API 추가
    @PostMapping("/{markerId}/update")
    public ResponseEntity<?> update(@PathVariable Long markerId,
        @ModelAttribute MarkerDTO markerDTO) {
        try {
            MarkerDTO updatedMarkerDTO = markerService.updateMarker(markerId, markerDTO);
            return ResponseEntity.ok(updatedMarkerDTO);
        } catch (IOException e) {
            log.error("File upload error during marker update: ", e);
            return ResponseEntity.internalServerError().body("File upload error during board update");
        }

    }
}


