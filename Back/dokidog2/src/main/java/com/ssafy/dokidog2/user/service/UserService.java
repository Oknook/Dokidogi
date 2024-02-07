package com.ssafy.dokidog2.user.service;

import com.ssafy.dokidog2.user.dto.GrassDTO;
import com.ssafy.dokidog2.user.entity.Grass;
import com.ssafy.dokidog2.user.repository.GrassRepository;
import com.ssafy.dokidog2.user.dto.PetDTO;
import com.ssafy.dokidog2.user.dto.UserDTO;
import com.ssafy.dokidog2.user.entity.Pet;
import com.ssafy.dokidog2.user.entity.User;
import com.ssafy.dokidog2.user.repository.PetRepository;
import com.ssafy.dokidog2.user.repository.RelationRepository;
import com.ssafy.dokidog2.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    @Value("${spring.jwt.secret}")
    private String secretKey;

    private final UserRepository userRepository;
    private final RelationRepository relationRepository;
    private final GrassRepository grassRepository;
    public UserService(UserRepository userRepository, PetRepository petRepository, RelationRepository relationRepository, GrassRepository grassRepository) {
        this.userRepository = userRepository;
        this.relationRepository = relationRepository;
        this.grassRepository = grassRepository;
    }
    public void associate(long userId, UserDTO userDTO) {
        User user = userRepository.findByUserId(userId);
        user.setSex(userDTO.getSex());
        user.setNickname(userDTO.getNickname());
        user.setBirth(userDTO.getBirth());
        user.setAddress(userDTO.getAddress());
    }
    public void modifyUserDetails(long id, UserDTO userDTO) {
        User member = userRepository.findByUserId(id);
        member.setSex(userDTO.getSex());
        member.setNickname(userDTO.getNickname());
        member.setBirth(userDTO.getBirth());
        member.setAddress(userDTO.getAddress());
    }

    public void signout(long id) {
        User member = userRepository.findByUserId(id);
        member.setDelYN(true);
    }

    public void putGrass() {
        LocalDateTime date = LocalDateTime.now();

        Random rand = new Random();
        User user = userRepository.findByUserId(1l);
        System.out.println(user.getNickname());
        System.out.println(user.getGrasses());

        for (int i = 0; i < 100; i++) {
            int ran = rand.nextInt(37);
            grassRepository.save(new Grass(user, date.minusDays(ran)));
        }
    }

    public JSONObject grassData(long userId) {
        List<GrassDTO> list = grassRepository.getGrassesByUserId(userId);
        HashMap<String, Long> map = new HashMap<>();
        for (GrassDTO g : list) {
            map.put(g.getDates().toString(), g.getCount());
        }
        HashMap<String, Integer> dayOfWeek = new HashMap<>();
        dayOfWeek.put("MONDAY", 1);
        dayOfWeek.put("TUESDAY", 2);
        dayOfWeek.put("WEDNESDAY", 3);
        dayOfWeek.put("THURSDAY", 4);
        dayOfWeek.put("FRIDAY", 5);
        dayOfWeek.put("SATURDAY", 6);
        dayOfWeek.put("SUNDAY", 7);

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime firstDayOfYear = LocalDateTime.now().minusDays(LocalDateTime.now().getDayOfYear()).plusDays(1);

        JSONObject data = new JSONObject();
        int index = 1;
        for (int i = 0; i < LocalDateTime.now().getDayOfYear(); i++) {
            if (map.containsKey(firstDayOfYear.plusDays(i).toLocalDate().toString())) {
                JSONObject day = new JSONObject();
                day.put("dayIndex", dayOfWeek.get(firstDayOfYear.plusDays(i).getDayOfWeek().toString()));
                day.put("date", firstDayOfYear.plusDays(i).toLocalDate().toString());
                day.put("value", map.get(firstDayOfYear.plusDays(i).toLocalDate().toString()));
                data.put(index, day);
                index++;
            }
        }

        return data;
    }

    // { code:200, message: "message" }
    public JSONObject successResponse(String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", HttpStatus.OK);
        jsonObject.put("message", message);
        return jsonObject;
    }

    public JSONObject failResponse(String message, HttpStatus status) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", status);
        jsonObject.put("message", message);
        return jsonObject;
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
        memberDTO.setNickname(member.getNickname());
        return memberDTO;
    }

    public PetDTO signintest2(long id) {
        List<Pet> relations = relationRepository.findPetsByUserId(id);

        PetDTO petDTO = new PetDTO();
        petDTO.setName(relations.get(0).getName());
        return petDTO;
    }

    public UserDTO getUserProfile(long userId) {
        UserDTO userDTO = new UserDTO();

        User user = userRepository.findByUserId(userId);
        userDTO.setNickname(user.getNickname());
        userDTO.setEmail(user.getEmail());
        userDTO.setSex(user.getSex());
        userDTO.setBirth(user.getBirth());
        userDTO.setAddress(user.getAddress());
        userDTO.setImageId(user.getImageId());
        userDTO.setGrass(grassData(userId));
        userDTO.setPets(relationRepository.findPetsByUserId(userId));
        return userDTO;
    }
    // blockservice 차단 등록, 목록, 해제 3개 해야 함!
}
