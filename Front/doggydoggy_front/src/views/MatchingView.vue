<template>
  <div>
    <!-- 반려견 주인의 성별 선택 -->
    <select v-model="sex">
      <option disabled value="">성별 선택</option>
      <option value="M">남성</option>
      <option value="F">여성</option>
      <option value="N">상관없음</option>
    </select>
    <!-- 반려견 주인의 나이 범주 선택 -->
    <select v-model="age">
      <option disabled value="">나이 범주 선택</option>
      <option value="20">20대</option>
      <option value="30">30대</option>
      <option value="40">40대</option>
      <option value="0">상관없음</option>
    </select>
    <!-- 반려동물 크기 선택 -->
    <select v-model="size">
      <option disabled value="">반려동물 크기 선택</option>
      <option value="0">작음</option>
      <option value="1">중간</option>
      <option value="2">큼</option>
      <option value="3">상관없음</option>
    </select>

    <!-- 탐색 버튼 -->
    <button @click="explore">탐색하기</button>

    <button @click="exploreRealTime">실시간 탐색하기</button>

    <!-- API 응답 데이터 배열 표시 -->
    <div v-for="item in responseDataArray" :key="item.id">
      <!--      <p>ID: {{ item.id }}</p>-->
      <p>이름: {{ item.name }}</p>
      <!--      <p>정보: {{ item.info }}</p>-->
      <p>종류: {{
          (item.kind.toLowerCase().trim() === 'maltese' ? '말티즈' :
              item.kind.toLowerCase().trim() === 'poodle' ? '푸들' :
                  item.kind.toLowerCase().trim() === 'mix' ? '믹스견' :
                      item.kind.toLowerCase().trim() === 'pomeranian' ? '포메라니안' :
                          item.kind.toLowerCase().trim() === 'jindo' ? '진돗개' :
                              item.kind.toLowerCase().trim() === 'shihtzu' ? '시츄' :
                                  item.kind.toLowerCase().trim() === 'bichonfrise' ? '비숑 프리제' :
                                      item.kind.toLowerCase().trim() === 'golden retriever' ? '골든 리트리버' :
                                          item.kind.toLowerCase().trim() === 'dachshund' ? '닥스훈트' :
                                              item.kind.toLowerCase().trim() === 'greyhound' ? '그레이하운드' :
                                                  item.kind.toLowerCase().trim() === 'beagle' ? '비글' :
                                                      item.kind.toLowerCase().trim() === 'husky' ? '허스키' :
                                                          item.kind.toLowerCase().trim() === 'shepherd' ? '셰퍼드' : '알 수 없음')
        }}</p>
      <p>크기: {{
          item.size === 0 ? '소형' :
              item.size === 1 ? '중형' :
                  item.size === 2 ? '대형' : '알 수 없음'
        }}</p>
      <p>성별: {{
          item.sex === 'F' ? '암컷' :
              item.sex === 'M' ? '수컷' : '알 수 없음'
        }}</p>
      <!--      <p>Neuter: {{ item.neuter }}</p>-->
      <p>나이: {{ item.age }}살</p>
      <br>
    </div>


  </div>
</template>

<script>
import axios from 'axios';
import { ref } from 'vue';

export default {
  setup() {
    const size = ref("3");
    const age = ref("");
    const sex = ref("N");
    const responseDataArray = ref([]);

    // 기존 탐색 함수
    const explore = () => {
      axios.post('/api/match/2', {
        sex: sex.value,
        age: age.value === "" ? null : age.value,
        size: size.value
      })
      .then(response => {
        responseDataArray.value = response.data;
      })
      .catch(error => {
        console.error(error);
      });
    };

    // 실시간 근처 탐색 함수
    const exploreRealTime = async () => {
      try {
        const position = await getCurrentLocation();
        const userLatitude = position.coords.latitude;
        const userLongitude = position.coords.longitude;
        console.log(userLatitude)
        console.log(userLongitude)

        axios.post('/api/match/realtime', {
          sex: sex.value,
          age: age.value === "" ? null : age.value,
          size: size.value,
          latitude: userLatitude,
          longitude: userLongitude
        })
        .then(response => {
          responseDataArray.value = response.data;
          console.log("실시간" + responseDataArray.value)
        })
        .catch(error => {
          console.error(error);
        });
      } catch (error) {
        console.error("위치 정보를 가져올 수 없습니다.", error);
      }
    };

    // 사용자의 현재 위치를 얻는 함수
    function getCurrentLocation() {
      return new Promise((resolve, reject) => {
        navigator.geolocation.getCurrentPosition(resolve, reject, {
          maximumAge: 600000,
          timeout: 5000,
          enableHighAccuracy: true,
        });
      });
    }

    return {
      size,
      age,
      sex,
      explore,
      exploreRealTime,
      responseDataArray,
    };
  },
};
</script>