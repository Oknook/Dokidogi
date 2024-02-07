package com.ssafy.dokidog2.matching.service;

import com.ssafy.dokidog2.matching.dto.MatchDTO;
import com.ssafy.dokidog2.user.dto.PetDTO;
import com.ssafy.dokidog2.user.entity.Relation;
import com.ssafy.dokidog2.user.entity.User;
import com.ssafy.dokidog2.user.repository.PetRepository;
import com.ssafy.dokidog2.user.repository.RelationRepository;
import com.ssafy.dokidog2.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final RelationRepository relationRepository;

    // 주변 2km 유저 탐색하는 함수 UserRepository에서 정의
    public List<User> findUsersWithin2km(double latitude, double longitude) {
        return userRepository.findUsersWithinDistance(latitude, longitude, 800.0);
    }

//    // 생년월일 기준 나이로 변환하는 함수
//    public int calculateAge(int birth) {
////
//        // int 타입의 birth를 String으로 변환
//        String birthDateStr = String.format("%06d", birth);
//
//        // YYMMDD 형식의 생년월일을 LocalDate 객체로 파싱
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
//        LocalDate birthDateLocal = LocalDate.parse(birthDateStr, formatter);
//
//        // 2000년 이전과 이후의 구분 처리
//        // 예를 들어, 990101은 1999년 1월 1일이 되어야 하고, 010101은 2001년 1월 1일이 되어야 합니다.
//        // 만약 파싱된 날짜가 미래 날짜로 계산된다면, 100년을 빼서 올바른 날짜로 조정합니다.
//        if (birthDateLocal.isAfter(LocalDate.now())) {
//            birthDateLocal = birthDateLocal.minusYears(100);
//        }
//
//        // 오늘 날짜를 기준으로 나이 계산
//        long age = ChronoUnit.YEARS.between(birthDateLocal, LocalDate.now());
//
//        return (int) age;
//    }

    // 필터링 함수 MatchDTO는 프론트에서 받아오는 매칭 필터링 데이터 // userInList 는 주변에 거주하는 사용자 리스트
    public List<PetDTO> filterPets(MatchDTO matchDTO, List<User> userInList) {
        // 사용자 ID를 필터링합니다.
        List<Long> filteredUserIds = userInList.stream()
            // 성별 상관없음 == null 값 받기 때문에
            .filter(user -> matchDTO.getSex() == null || user.getSex() == matchDTO.getSex())
            // 나이 0으로 설정한 경우가 상관없음으로 표시
            .filter(user -> matchDTO.getAge() == 0
                || user.calculateAge(user.getBirth()) == matchDTO.getAge())
            // userId만 받는 리스트로 만든다
            .map(User::getUserId)
            // 필터들을 통해 만들어진게 합쳐진다
            .collect(Collectors.toList());
        System.out.println("1차 필터링");
        System.out.println(filteredUserIds);
        // RelationRepository를 사용하여 필터링된 사용자 ID에 해당하는 RelationEntity를 조회합니다.

        List<Relation> relations = relationRepository.findRelationsby(filteredUserIds);
        System.out.println("관계 필터링");
        System.out.println(relations);

        // 조회된 RelationEntity로부터 petId만 추출합니다.
        List<Long> petIds = relations.stream()
            .map(relation -> relation.getPet().getPetId())
//            .distinct() // 중복 제거 해야 하나??
            .collect(Collectors.toList());
        System.out.println("펫");
        System.out.println(petIds);
        // PetRepository를 사용하여 petId에 해당하는 PetEntity 리스트를 조회합니다.
        // 이 과정에서 petSize 조건에 따라 필터링합니다.
        // 팻 사이즈 0: 소형 1: 중형 2: 대형 3: 상관 없음
        List<PetDTO> filteredPetsList = petRepository.findAllById(petIds).stream()
            .filter(pet -> matchDTO.getSize() == 3 || pet.getSize() == matchDTO.getSize())
            .map(pet -> new PetDTO(pet)) // PetEntity를 PetDTO로 변환
            .collect(Collectors.toList());
        System.out.println("필터링 펫");
        System.out.println(filteredPetsList);
        return filteredPetsList;
    }

    public void updateGenders() {
        ArrayList<Long> userIds = new ArrayList<>();
        for (long i = 1; i <= 300; i++) {
            userIds.add(i);
        }
        System.out.println(userIds);

        List<User> users = userRepository.findAllById(userIds);
        System.out.println(users);

        Random random = new Random();
        for (User user : users) {
            if (random.nextInt() % 3 == 0) {
                user.setSex(false);
            }
            else {
                user.setSex(true);
            }
            userRepository.save(user);
        }
    }

    /*
     public List<User> findByCriteria(UserPetFilterDTO filter) {
        Specification<User> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getAge() != null) {
                predicates.add(cb.equal(root.get("age"), filter.getAge()));
            }
            if (!"상관없음".equals(filter.getGender())) {
                predicates.add(cb.equal(root.get("gender"), filter.getGender()));
            }
            if (!"상관없음".equals(filter.getPetSize())) {
                // 애완동물 크기에 따른 필터링은 조금 더 복잡한 로직을 요구할 수 있음
                // 예를 들어, pets 컬렉션을 조인하여 크기를 필터링하는 방식 등
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return userRepository.findAll(spec);
    }
     */

}
