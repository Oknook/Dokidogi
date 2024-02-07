package com.ssafy.dokidog2.user;

import com.ssafy.dokidog2.user.dto.PetDTO;
import com.ssafy.dokidog2.user.dto.UserDTO;
import com.ssafy.dokidog2.user.service.PetService;
import com.ssafy.dokidog2.user.service.UserService;
import com.ssafy.dokidog2.util.JwtTokenProvider;
import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final PetService petService;
    private final JwtTokenProvider jwtTokenProvider;
    public UserController(UserService userService, PetService petService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.petService = petService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/profile")
    public JSONObject grass() {
        JSONObject result = userService.grassData(1);
        return result;
    }

    @GetMapping("/profile2")
    public String grasss() {
        userService.putGrass();
        return "put";
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestParam(name = "select") int select) {
        /*
        header
        {
          "alg": "HS256",
          "typ": "JWT"
        }
        .
        {
          "userId": "1",
          "accessToken": "access_allowed"
        }
        .
        HMACSHA256(
          base64UrlEncode(header) + "." +
          base64UrlEncode(payload),
        kimmoonkwanhecanrightfullybecalledalegend
        )
         */
        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIxIiwiYWNjZXNzVG9rZW4iOiJhY2Nlc3NfYWxsb3dlZCJ9.wVPMRMwF1nzKcI_D7eQX6Ysg6izLml7zB8yKMi4zLFQ";
        String jwt2 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyIiwiYWNjZXNzVG9rZW4iOiJhY2Nlc3NfYWxsb3dlZCJ9.ACLN1vSQ9yn9dsPzA3MlJ0U3FAprxwb95XxblHWMrHA";
        HttpHeaders headers = new HttpHeaders();
        if (select == 1) {
            headers.add("Authorization", "Bearer " + jwt);
        }
        else {
            headers.add("Authorization", "Bearer " + jwt2);
        }
        return ResponseEntity.ok()
            .headers(headers)
            .body("tmpLogin");
    }

    @PutMapping("/signup/associate")
    public ResponseEntity<JSONObject> associateMember(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(name = "userDTO") UserDTO userDTO) {
        String jwt = authorizationHeader.replace("Bearer ", "");
        JSONObject response;

        if (userDTO.getSex() == null || userDTO.getNickname() == null || userDTO.getBirth() == null || userDTO.getAddress() == null) {
            response = userService.failResponse("모든 정보 입력 요망", HttpStatus.FORBIDDEN);
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
        }
        long userId = jwtTokenProvider.getUserId(jwt);
        userService.associate(userId, userDTO);

        response = userService.successResponse("준회원 전환 성공");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }

    @PostMapping("/signup/regular")
    public void regular(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(name = "birth")int birth, @RequestParam(name = "number")long number) {
        String jwt = authorizationHeader.replace("Bearer ", "");
        long userId = jwtTokenProvider.getUserId(jwt);
        // API 확인 만들기 -> 공공데이터 신청 해야 함!
    }

    @PutMapping("/profile/modify")
    public ResponseEntity<JSONObject> modifyMember(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(name = "userDTO") UserDTO userDTO) {
        String jwt = authorizationHeader.replace("Bearer ", "");

        long userId = jwtTokenProvider.getUserId(jwt);
        userService.modifyUserDetails(userId, userDTO);

        JSONObject response = userService.successResponse("정보 수정 완료");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }

    @DeleteMapping("/signout")
    public ResponseEntity<JSONObject> signoutMember(@RequestHeader("Authorization") String authorizationHeader) {
        String jwt = authorizationHeader.replace("Bearer ", "");

        long userId = jwtTokenProvider.getUserId(jwt);
        userService.signout(userId);

        JSONObject response = userService.successResponse("정보 수정 완료");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }

    @GetMapping("/profile/owner")
    public UserDTO userProfile(@RequestHeader("Authorization") String authorizationHeader) {
        String jwt = authorizationHeader.replace("Bearer ", "");
        long userId = jwtTokenProvider.getUserId(jwt);
        return userService.getUserProfile(userId);
    }

    @GetMapping("/profile/dog")
    public PetDTO petProfile(@RequestParam(name = "petId")long petId) {
        return petService.getPetProfile(petId);
    }

    @GetMapping("/test")
    public UserDTO test(@RequestParam(name = "id") long id) {
        UserDTO res = userService.signintest(id);
        return res;
    }

    @GetMapping("/test2")
    public PetDTO test2(@RequestParam(name = "id") long id) {
        PetDTO res = userService.signintest2(id);
        return res;
    }
}
