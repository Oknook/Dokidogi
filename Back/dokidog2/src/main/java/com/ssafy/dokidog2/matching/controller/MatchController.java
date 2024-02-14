package com.ssafy.dokidog2.matching.controller;


import com.ssafy.dokidog2.matching.dto.MatchDTO;
import com.ssafy.dokidog2.matching.service.MatchService;
import com.ssafy.dokidog2.user.dto.PetDTO;
import com.ssafy.dokidog2.user.entity.User;
import com.ssafy.dokidog2.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MatchController {

    private final MatchService matchService;
    private final UserRepository userRepository;


    // 매칭 요청 클릭시 매칭 필터
    @PostMapping("/api/match/{userId}")
    public ResponseEntity<?> match(@PathVariable Long userId, @RequestBody MatchDTO matchDTO) {
        System.out.println(userId);
        System.out.println(matchDTO);

        // userId에 맞는 데이터 찾기
        Optional<User> optionalUser = userRepository.findById(userId);
        User userMap = optionalUser.get();
        double latitude = userMap.getLatitude(); // 위도 가져오기
        double longitude = userMap.getLongitude(); // 경도 가져오기

        // 주변 2km이내 사용자 리스트 필터링
        List<User> userInList = matchService.findUsersWithin2km(latitude, longitude);
        System.out.println("주변 사용자");
        System.out.println(userInList);

        // 필터링된 반려동물 목록 가져오기
        List<PetDTO> filteredPets = matchService.filterPets(matchDTO, userInList);

        return ResponseEntity.ok(filteredPets);
    }

//    @GetMapping("/api/test")
//    public String test () {
//        matchService.updateGenders();
//
//        System.out.println("!");
//        return "hi";
//    }


    // 재 탐색 >> 현재 gps 프론트에서 값 주고 위의 코드랑 똑같이 생각하면 될듯?




}
