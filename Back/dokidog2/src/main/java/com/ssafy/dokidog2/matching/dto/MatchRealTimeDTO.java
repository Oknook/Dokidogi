package com.ssafy.dokidog2.matching.dto;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class MatchRealTimeDTO {
    private int age;
    private char sex; // 견주 성별
    private byte size; // 애견 소형, 중형, 대형, 상관없음
    private double latitude;
    private double longitude;


    @Builder
    public MatchRealTimeDTO(int age, char sex, byte size, double latitude, double longitude){
        this.age = age;
        this.sex = sex;
        this.size = size;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
