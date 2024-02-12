package com.ssafy.dokidog2.user;

import com.ssafy.dokidog2.user.dto.PetDTO;
import com.ssafy.dokidog2.user.dto.PutPetDTO;
import com.ssafy.dokidog2.user.dto.PutUserDTO;
import com.ssafy.dokidog2.user.dto.UserDTO;
import com.ssafy.dokidog2.user.service.BlockService;
import com.ssafy.dokidog2.user.service.PetService;
import com.ssafy.dokidog2.user.service.UserService;
import com.ssafy.dokidog2.util.JwtTokenProvider;
import com.ssafy.dokidog2.util.Response;
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
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final PetService petService;
    private final JwtTokenProvider jwtTokenProvider;
    private final BlockService blockService;
    public UserController(UserService userService, PetService petService, JwtTokenProvider jwtTokenProvider, BlockService blockService) {
        this.userService = userService;
        this.petService = petService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.blockService = blockService;
    }

    @PutMapping("/user/signup")
    public Response associateMember(@RequestHeader("Authorization") String authorizationHeader, @RequestParam PutUserDTO dto) {
        String jwt = authorizationHeader.replace("Bearer ", "");
        Long id = jwtTokenProvider.getUserId(jwt);

        return Response.builder()
            .code("200")
            .message("서버 응답")
            .data(userService.associate(id, dto))
            .build();
    }

    @PostMapping("/user/signup")
    public Response regularMember(@RequestHeader("Authorization") String authorizationHeader, @RequestParam PutPetDTO dto) {
        String jwt = authorizationHeader.replace("Bearer ", "");
        long userId = jwtTokenProvider.getUserId(jwt);

        return Response.builder()
            .code("200")
            .message("서버 응답")
            .data(petService.regular(userId, dto))
            .build();
    }

    @PutMapping("/user/modify")
    public Response modifyMember(@RequestHeader("Authorization") String authorizationHeader, @RequestParam PutUserDTO dto) {
        String jwt = authorizationHeader.replace("Bearer ", "");
        long userId = jwtTokenProvider.getUserId(jwt);

        return Response.builder()
            .code("200")
            .message("서버 응답")
            .data(userService.modifyMember(userId, dto))
            .build();
    }

    @PutMapping("/pet/modify")
    public Response modifyPet(@RequestParam PutPetDTO dto) {
        return Response.builder()
            .code("200")
            .message("서버 응답")
            .data(petService.modifyPet(dto))
            .build();
    }

    @DeleteMapping("/user/signout")
    public Response signoutMember(@RequestHeader("Authorization") String authorizationHeader) {
        String jwt = authorizationHeader.replace("Bearer ", "");
        long userId = jwtTokenProvider.getUserId(jwt);

        return Response.builder()
            .code("200")
            .message("서버 응답")
            .data(userService.signout(userId))
            .build();
    }

    @GetMapping("/user/profile")
    public Response userProfile(@RequestHeader("Authorization") String authorizationHeader) {
        String jwt = authorizationHeader.replace("Bearer ", "");
        long userId = jwtTokenProvider.getUserId(jwt);
        return Response.builder()
            .code("200")
            .message("서버 응답")
            .data(userService.getUserProfile(userId))
            .build();
    }

    @GetMapping("/pet/profile")
    public Response petProfile(@RequestParam(name = "petId") long petId) {
        return Response.builder()
            .code("200")
            .message("서버 응답")
            .data(petService.getPetProfile(petId))
            .build();
    }

    @PostMapping("/sanction/block")
    public Response block(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(name = "blockId") long blockId) {
        String jwt = authorizationHeader.replace("Bearer ", "");
        long userId = jwtTokenProvider.getUserId(jwt);

        return Response.builder()
            .code("200")
            .message("서버 응답")
            .data(blockService.block(userId, blockId))
            .build();
    }
    @GetMapping("/sanction/block")
    public Response getBlockList(@RequestHeader("Authorization") String authorizationHeader) {
        String jwt = authorizationHeader.replace("Bearer ", "");
        long userId = jwtTokenProvider.getUserId(jwt);

        return Response.builder()
            .code("200")
            .message("서버 응답")
            .data(blockService.getList(userId))
            .build();
    }
    @DeleteMapping("/sanction/block")
    public Response unblock(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(name = "blockId") long blockId) {
        String jwt = authorizationHeader.replace("Bearer ", "");
        long userId = jwtTokenProvider.getUserId(jwt);

        return Response.builder()
            .code("200")
            .message("서버 응답")
            .data(blockService.unblock(userId, blockId))
            .build();
    }
//
//    @PostMapping("/signin")
//    public ResponseEntity<String> signin(@RequestParam(name = "select") int select) {
//        /*
//        header
//        {
//          "alg": "HS256",
//          "typ": "JWT"
//        }
//        .
//        {
//          "userId": "1",
//          "accessToken": "access_allowed"
//        }
//        .
//        HMACSHA256(
//          base64UrlEncode(header) + "." +
//          base64UrlEncode(payload),
//        kimmoonkwanhecanrightfullybecalledalegend
//        )
//         */
//        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIxIiwiYWNjZXNzVG9rZW4iOiJhY2Nlc3NfYWxsb3dlZCJ9.wVPMRMwF1nzKcI_D7eQX6Ysg6izLml7zB8yKMi4zLFQ";
//        String jwt2 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyIiwiYWNjZXNzVG9rZW4iOiJhY2Nlc3NfYWxsb3dlZCJ9.ACLN1vSQ9yn9dsPzA3MlJ0U3FAprxwb95XxblHWMrHA";
//        HttpHeaders headers = new HttpHeaders();
//        if (select == 1) {
//            headers.add("Authorization", "Bearer " + jwt);
//        }
//        else {
//            headers.add("Authorization", "Bearer " + jwt2);
//        }
//        return ResponseEntity.ok()
//            .headers(headers)
//            .body("tmpLogin");
//    }

//    @GetMapping("/test")
//    public UserDTO test(@RequestParam(name = "id") long id) {
//        UserDTO res = userService.signintest(id);
//        return res;
//    }
//
//    @GetMapping("/test2")
//    public PetDTO test2(@RequestParam(name = "id") long id) {
//        PetDTO res = userService.signintest2(id);
//        return res;
//    }
}
