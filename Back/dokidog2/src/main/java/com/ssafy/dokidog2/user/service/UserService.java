package com.ssafy.dokidog2.user.service;

import com.ssafy.dokidog2.user.dto.UserDTO;
import com.ssafy.dokidog2.user.entity.User;
import com.ssafy.dokidog2.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO signin(UserDTO userDTO) {
        User member = userRepository.findByUserId(userDTO.getId());
        UserDTO memberDTO = new UserDTO();
        memberDTO.setId(member.getUserId());
        memberDTO.setNickname(member.getNickname());
        return memberDTO;
    }

    public UserDTO signintest(long id) {
        User member = userRepository.findByUserId(id);
        UserDTO memberDTO = new UserDTO();
        memberDTO.setId(member.getUserId());
        memberDTO.setNickname(member.getNickname());
        return memberDTO;
    }
}
