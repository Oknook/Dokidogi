package com.ssafy.dokidog2.user;

import com.ssafy.dokidog2.user.dto.UserDTO;
import com.ssafy.dokidog2.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping("/test")
//    @ResponseBody
//    public UserDTO test(@RequestParam(name = "dto") UserDTO userDTO) {
//        UserDTO res = userService.signin(userDTO);
//        return res;
//    }

    @GetMapping("/test")
    @ResponseBody
    public UserDTO test(@RequestParam(name = "id") long id) {
        UserDTO res = userService.signintest(id);
        return res;
    }
}
