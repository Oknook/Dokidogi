<template>
  <div class="">
    <!-- 드롭다운을 가로로 배치하기 위한 컨테이너 추가 -->
    <div class="main-container">
      <!-- 반려견 주인의 성별 선택 -->
      <select v-model="sex">
        <option disabled value="">견주 성별</option>
        <option value="M">남성</option>
        <option value="F">여성</option>
        <option value="N">상관없음</option>
      </select>
      <!-- 반려견 주인의 나이 범주 선택 -->
      <select v-model="age">
        <option disabled value="">견주 나이대</option>
        <option value="20">20대</option>
        <option value="30">30대</option>
        <option value="40">40대</option>
        <option value="0">상관없음</option>
      </select>
      <!-- 반려동물 크기 선택 -->
      <select v-model="size">
        <option disabled value="">반려동물 크기</option>
        <option value="0">소형</option>
        <option value="1">중형</option>
        <option value="2">대형</option>
        <option value="3">상관없음</option>
      </select>
    </div>

    <!-- 탐색 버튼 -->
    <!--    <button @click="explore">탐색하기</button>-->
    <div class="button-container">
      <button class="search-btn" @click="exploreRealTime">실시간 탐색하기</button>
    </div>
    <!-- API 응답 데이터 배열 표시 -->
    <div class="container-box" v-for="item in responseDataArray" :key="item.id">

      <div class="preview-container" @click="triggerFileInput">
        <img class="placeholder-image" :src="`https://donghotest.s3.ap-northeast-2.amazonaws.com/${item.id}.jpg`" alt="Pet Image">
      </div>

      <div class="input-container">
        <!--      <p>ID: {{ item.id }}</p>-->
        <p>이름: {{ item.name }}</p>
        <!--      <p>정보: {{ item.info }}</p>-->
        <p>종류: {{
            (item.kind.toLowerCase().trim() === 'bordercolly' ? '보더콜리' :
                item.kind.toLowerCase().trim() === 'maltese' ? '말티즈' :
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


  </div>
</template>

<script>
import axios from 'axios';
import { ref } from 'vue';

export default {
  setup() {
    const size = ref("");
    const age = ref("");
    const sex = ref("");
    const responseDataArray = ref([]);

    // 기존 탐색 함수
    const explore = () => {
      axios.post('/api/match/2', {
        sex: sex.value === "" ? "N" : sex.value,
        age: age.value === "" ? null : age.value,
        size: size.value === "" ? 3 : size.value,
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
          sex: sex.value === "" ? "N" : sex.value,
          age: age.value === "" ? null : age.value,
          size: size.value === "" ? 3 : size.value,
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

    const triggerFileInput = () => {
      // 사용자를 다른 URL로 리다이렉트합니다.
      window.location.href = 'http://3.19.32.200:8088/';
    };


    return {
      size,
      age,
      sex,
      // explore,
      exploreRealTime,
      responseDataArray,
      triggerFileInput,
    };
  },
};
</script>

<style scoped>
.main-container{
  max-width: 1200px; /* 전체 컨테이너의 최대 너비 */
  max-height: 1200px;
  margin: 0 auto; /* 페이지 중앙 정렬 */
  padding: 20px; /* 내부 여백 */
}

.dropdown-container {
  display: flex;
  justify-content: center; /* 드롭다운과 버튼을 중앙 정렬 */
  align-items: center; /* 세로축으로도 중앙 정렬 */
  flex-wrap: nowrap; /* 내용이 넘칠 경우 다음 줄로 이동 안하게  */
  gap: 20px; /* 드롭다운과 버튼 사이의 간격 */
  margin-bottom: 20px;
}

.container-box {
  background-color: #d9eed2; /* white background */
  border: 1px solid #e1e1e1; /* light grey border */
  border-radius: 8px; /* rounded corners */
  padding: 30px; /* spacing inside the box */
  margin: 20px 0; /* spacing outside the box */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* subtle shadow */
  width: 90%;
  margin-left: auto; /* centering the box */
  margin-right: auto;
  display: flex;
  height: 200px;
  justify-content: space-between; /* Align children to opposite ends */
  font-family: 'Noto Sans KR', sans-serif;
}

.preview-container {
  flex-basis: 40%; /* Adjust this value as needed */
  max-height: 100px; /* Adjust this value as needed */
}

.input-container {
  flex-basis: 58%; /* Adjust this value as needed, making sure the total with preview is less than 100% */
  display: flex;
  flex-direction: column; /* Stack the inputs vertically */
  justify-content: space-between; /* This will ensure space between inputs and file upload */
  font-family: 'Noto Sans KR', sans-serif;
  font-size: medium;
}

.preview-image {
  /* 이미지 미리보기의 스타일을 지정합니다. */
  max-width: 80%; /* 이미지의 최대 폭을 설정합니다. */
  max-height: 150px; /* 이미지의 최대 높이를 설정합니다. */
  margin-top: 10px; /* 이미지 위에 공간을 추가합니다. */
  margin-left: 20px;
}

p {
  margin: 4px;
}

.placeholder-image {
  /* Adjust the size as needed */
  width: 150px; /* Example size */
  height: 150px; /* Example size */
  cursor: pointer; /* Indicates that the image is clickable */
  margin-left: 20px;
  border-radius: 15px;
}

.button-container {
  display: flex; /* Flexbox 레이아웃 사용 */
  justify-content: center; /* 가로축에서 중앙 정렬 */
  margin: 20px 0; /* 상단과 하단에 여백 추가 */
}

.search-btn {
  background-color: #D9EED2;
  font-family: "Noto Sans KR";
  font-weight: 600;
  width: 120px;
  height: 40px;
  color: black;
  border: none;
  border-radius: 7px;
}

select {
  -moz-appearance: none;
  -webkit-appearance: none;
  appearance: none;

  font-family: "Noto Sans KR", sans-serif; /* 폰트 패밀리 오타 수정 */
  font-size: 1rem;
  font-weight: 400;
  line-height: 1.5;

  color: #444;
  background-color: #fff;

  padding: .6em 1.4em .5em .8em;
  margin: 0;

  border: 1px solid #aaa;
  border-radius: .5em;
  box-shadow: 0 1px 0 1px rgba(0,0,0,.04);

  width: calc(33% - 10px); /* 컨테이너 너비의 1/3에서 양 옆 마진을 고려한 값으로 조정 */
  cursor: pointer; /* 드롭다운 화살표 대신 커서 변경으로 클릭 가능 표시 */
  text-align-last: center; /* 드롭다운 내의 글씨를 중앙 정렬 */
}

select:hover {
  border-color: #888;
}

select:focus {
  border-color: #aaa;
  box-shadow: 0 0 1px 3px rgba(59, 153, 252, .7);
  color: #222;
  outline: none;
}

select:disabled {
  opacity: 0.5;
}

label {
  font-family: "Noto Sans KR", sans-serif;
  font-size: 1rem;
  font-weight: 600;
  line-height: 1.3;

  color: #444;

  margin-right: 0.5em;
}

/* 추가된 라벨과 입력 필드 스타일 */
.form__label, .form__input {
  /* 스타일 정의 */
}

</style>