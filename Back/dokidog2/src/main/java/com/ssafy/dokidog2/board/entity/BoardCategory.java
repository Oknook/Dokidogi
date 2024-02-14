package com.ssafy.dokidog2.board.entity;

public enum BoardCategory {
    자유, 자랑, 정보, 산책후기 ;

    // 문자열로부터 해당 BoardCategory를 찾는 메서드
    public static BoardCategory of(String category) {
        // 주어진 문자열을 이용하여 해당하는 BoardCategory 반환
        for (BoardCategory value : BoardCategory.values()) {
            if (value.name().equals(category)) {
                return value;
            }
        }
        // 일치하는 카테고리가 없을 경우 null 반환
        return null;
    }

}

