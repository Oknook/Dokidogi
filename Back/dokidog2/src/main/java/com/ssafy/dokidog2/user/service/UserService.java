package com.ssafy.dokidog2.user.service;

import com.ssafy.dokidog2.user.dto.GrassDTO;
import com.ssafy.dokidog2.user.dto.PutUserDTO;
import com.ssafy.dokidog2.user.entity.Grass;
import com.ssafy.dokidog2.user.repository.GrassRepository;
import com.ssafy.dokidog2.user.dto.PetDTO;
import com.ssafy.dokidog2.user.dto.UserDTO;
import com.ssafy.dokidog2.user.entity.Pet;
import com.ssafy.dokidog2.user.entity.User;
import com.ssafy.dokidog2.user.repository.PetRepository;
import com.ssafy.dokidog2.user.repository.RelationRepository;
import com.ssafy.dokidog2.user.repository.UserRepository;
import com.ssafy.dokidog2.util.Response;
import com.ssafy.dokidog2.util.UserGrade;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final PetRepository petRepository;
    public UserService(UserRepository userRepository, RelationRepository relationRepository, GrassRepository grassRepository, PetRepository petRepository) {
        this.userRepository = userRepository;
        this.relationRepository = relationRepository;
        this.grassRepository = grassRepository;
        this.petRepository = petRepository;
    }
    
    public Response associate(long userId, PutUserDTO dto) {
        Response response = new Response();
        try {
            User user = userRepository.findByUserId(userId);
            user.setBirth(dto.getBirth());
            user.setNickname(dto.getNickname());
            user.setSex(dto.getSex());
            user.setLatitude(dto.getLatitude());
            user.setLongitude(dto.getLongitude());
            if (user.getGrade().equals(UserGrade.TEMPORARY)) user.setGrade(UserGrade.ASSOCIATE);
            userRepository.save(user);

            response.builder()
                .code("200")
                .message("입력 성공")
                .build();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            response.builder()
                .code("400")
                .message("입력 오류")
                .build();
        }
        return response;
    }

    public Response modifyMember (long userId, PutUserDTO dto) {
        Response response = new Response();
        try {
            User user = userRepository.findByUserId(userId);
            if (dto.getBirth() != null) user.setBirth(dto.getBirth());
            if (dto.getNickname() != null) user.setNickname(dto.getNickname());
            if (dto.getSex() != null) user.setSex(dto.getSex());
            if (dto.getLatitude() != null) user.setLatitude(dto.getLatitude());
            if (dto.getLongitude() != null) user.setLongitude(dto.getLongitude());
            userRepository.save(user);

            response.builder()
                .code("200")
                .message("입력 성공")
                .build();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            response.builder()
                .code("400")
                .message("입력 오류")
                .build();
        }
        return response;
    }

    public Response signout(long id) {
        User member = userRepository.findByUserId(id);
        member.setDelYN(true);
        return Response.builder()
            .code("200")
            .message("탈퇴 성공")
            .build();
    }

    public Response getUserProfile(long userId) {
        User user = userRepository.findByUserId(userId);

        return Response.builder()
            .code("200")
            .message("유저 조회")
            .data(UserDTO.builder()
                .userId(user.getUserId())
                .grade(user.getGrade())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .sex(user.getSex())
                .birth(user.getBirth())
                .latitude(user.getLatitude())
                .longitude(user.getLongitude())
                .grass(grassData(userId))
                .pets(petRepository.findMyPets(userId))
                .imageId(user.getImageId())
                .point(user.getPoint())
                .delYN(user.getDelYN())
                .regDttm(user.getRegDttm())
                .modDttm(user.getModDttm())
                .build())
            .build();
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

//    public void updateGenders() {
//        ArrayList<Long> userIds = new ArrayList<>();
//        for (long i = 1; i <= 300; i++) {
//            userIds.add(i);
//        }
//        List<User> users = userRepository.findAllById(userIds);
//        Random random = new Random();
//        for (User user : users) {
//            if (random.nextInt() % 2 == 0) {
//                user.setSex(false);
//            }
//            else {
//                user.setSex(true);
//            }
//        }
//    }
//
//    public void putGrass() {
//        LocalDateTime date = LocalDateTime.now();
//
//        Random rand = new Random();
//        User user = userRepository.findByUserId(1l);
//        System.out.println(user.getNickname());
//        System.out.println(user.getGrasses());
//
//        for (int i = 0; i < 100; i++) {
//            int ran = rand.nextInt(37);
//            grassRepository.save(new Grass(user, date.minusDays(ran)));
//        }
//    }

//    public UserDTO signin(UserDTO userDTO) {
//        User member = userRepository.findByUserId(userDTO.getId());
//        UserDTO memberDTO = new UserDTO();
//        memberDTO.setId(member.getUserId());
//        memberDTO.setNickname(member.getNickname());
//        return memberDTO;
//    }
//
//    public UserDTO signintest(long id) {
//        User member = userRepository.findByUserId(id);
//        UserDTO memberDTO = new UserDTO();
//        memberDTO.setNickname(member.getNickname());
//        return memberDTO;
//    }
//
//    public PetDTO signintest2(long id) {
//        List<Pet> relations = relationRepository.findPetsByUserId(id);
//
//        PetDTO petDTO = new PetDTO();
//        petDTO.setName(relations.get(0).getName());
//        return petDTO;
//    }
}
