package com.ssafy.dokidog2.user.repository;

import com.ssafy.dokidog2.user.dto.GrassDTO;
import com.ssafy.dokidog2.user.entity.Grass;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GrassRepository extends JpaRepository<Grass, Long> {
    @Query("SELECT new com.ssafy.dokidog2.user.dto.GrassDTO(DATE(g.dates), COUNT(g.user.userId)) "
        + "FROM Grass g "
        + "WHERE g.user.userId = 1 "
        + "GROUP BY DATE(g.dates) "
        + "ORDER BY DATE(g.dates)")
    List<GrassDTO> getGrassesByUserId(@Param("userId") Long userId);
}
