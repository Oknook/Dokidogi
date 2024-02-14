package com.ssafy.dokidog2.user.repository;

import com.ssafy.dokidog2.user.dto.UserDTO;
import com.ssafy.dokidog2.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(Long id);

    // 위도, 경도, 거리 기준으로 distance 내의 모든 사용자 위치 검색하는 코드
    // from users 는 UserEntity 이름
    // 하버 사인 공식
    @Query(value = "SELECT * FROM users WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(latitude)) * cos(radians(longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(latitude)))) < :distance", nativeQuery = true)
    List<User> findUsersWithinDistance(double latitude, double longitude, double distance);

    @Query(value = "SELECT u.userId FROM users u where u.companyId = :companyId and u.companyName = :companyName")
    Long findByCompanyIdAndCompanyName(String companyId, String companyName);
}
