package com.ssafy.dokidog2.map.repository;


import com.ssafy.dokidog2.map.entity.MarkerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarkerRepository extends JpaRepository<MarkerEntity, Long> {

}
