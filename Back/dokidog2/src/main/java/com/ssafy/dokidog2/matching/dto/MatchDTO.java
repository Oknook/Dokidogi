package com.ssafy.dokidog2.matching.dto;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import lombok.Builder;
import lombok.Data;

@Data
public class MatchDTO {

//    private int birth; // 견주 생년월일 YYMMDD
    private int age;
    private char sex; // 견주 성별
    private byte size; // 애견 소형, 중형, 대형, 상관없음


    // 생년월일 기준 나이로 변환하는 함수
    public int calculateAge(int birth) {
        // int 타입의 birth를 String으로 변환
        String birthDateStr = String.format("%06d", birth);


        // 생년월일 형식 검증 추가
        if (birthDateStr.substring(2, 4).equals("00") || birthDateStr.substring(4, 6).equals("00")) {
            throw new DateTimeException("월 또는 일이 잘못되었습니다.");
        }

        // YYMMDD 형식의 생년월일을 LocalDate 객체로 파싱
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        LocalDate birthDateLocal = LocalDate.parse(birthDateStr, formatter);


        // 2000년 이전과 이후의 구분 처리
        // 예를 들어, 990101은 1999년 1월 1일이 되어야 하고, 010101은 2001년 1월 1일이 되어야 합니다.
        // 만약 파싱된 날짜가 미래 날짜로 계산된다면, 100년을 빼서 올바른 날짜로 조정합니다.
        if (birthDateLocal.isAfter(LocalDate.now())) {
            birthDateLocal = birthDateLocal.minusYears(100);
        }

        // 오늘 날짜를 기준으로 나이 계산
        long age = ChronoUnit.YEARS.between(birthDateLocal, LocalDate.now());

        return (int) age;
    }

    @Builder
    public MatchDTO(int age, char sex, byte size){
//        this.birth = birth;
        this.age = age;
        this.sex = sex;
        this.size = size;
    }

}
