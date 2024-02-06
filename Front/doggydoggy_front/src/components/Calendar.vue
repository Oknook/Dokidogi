<template>
    <div>
      <svg class="heatmap" :viewBox="viewBox">
        <!-- 월별 라벨 -->
        <text v-for="(month, index) in months" 
            :key="'month-' + index" 
            :x="getMonthXPosition(index)" 
            :y="monthLabelY" 
            class="month-label">
        {{ month }}
        </text>

        <!-- 데이터 포인트를 표현하는 사각형 -->
        <g v-for="(week, weekIndex) in data" :key="'week-' + weekIndex">
        <rect v-for="(value, dayIndex) in week" 
                :key="'day-' + weekIndex + '-' + dayIndex" 
                :x="getDayXPosition(weekIndex)" 
                :y="getDayYPosition(dayIndex)" 
                :width="squareSize" 
                :height="squareSize" 
                :fill="getColor(value)"
                v-tooltip="tooltip && value.count ? '${value.count} ${tooltip} on ${value.date}' : noDataText">
        </rect>
        </g>
      </svg>
    </div>
</template>
  
<script setup>
  import { ref, computed, onMounted } from 'vue';
  import axios from 'axios';

  const data = ref([]) //백엔드에서 받아온 데이터를 저장할 반응형 변수
  const backendUrl = 'https://'


  // 데이터를 가져오는 함수
  async function fetchData() {
    try {
        const response = await axios.get(backendUrl);
        data.value = response.data; // 백엔드에서 받은 데이터를 data 변수에 할당
    } catch (error) {
        console.error('Error fetching data:', error);
    }
  }

  // 컴포넌트가 마운트될 때 fetchData 함수 호출
  onMounted(fetchData);
  
  
  // Tooltip 플러그인 설정 (v-tooltip 설치 필요)
  import VTooltip from 'v-tooltip';
  import 'v-tooltip/dist/v-tooltip.css';
  
  // 히트맵 데이터와 설정
  const endDate = ref(new Date()); // 예시로 현재 날짜 사용
  const values = ref([]); // 여기에 히트맵 데이터를 입력하세요
  const max = ref(100); // 최대 값 예시
  const rangeColor = ref(['#ebedf0', '#9be9a8', '#40c463', '#30a14e', '#216e39']); // 색상 범위 예시
  const locale = ref({
    months: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
    days: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
    on: 'on',
    less: 'Less',
    more: 'More'
  });
  const tooltip = ref(true);
  // 박스에 들어갈 수 단위
  const tooltipUnit = ref('walks');
  const vertical = ref(false);
  const noDataText = ref('No data');
  
  // SVG 뷰박스 설정 로직
  const viewBox = computed(() => {
  // SVG 크기 및 위치 계산 로직
  const width = vertical.value ? 100 : 365; // 수직 또는 수평 방향에 따른 너비 설정 예시
  const height = 100; // 높이 설정 예시
  return `0 0 ${width} ${height}`;
  });
  
// 색상 계산 함수
  function getColor(value) {
    const ratio = value / max.value;
    if (ratio < 0.2) return '#ebedf0';
    if (ratio < 0.4) return '#9be9a8';
    if (ratio < 0.6) return '#40c463';
    if (ratio < 0.8) return '#30a14e';
    return '#216e39';
  }

  // Tooltip 디렉티브 등록
  VTooltip.options.defaultPlacement = 'top';
  VTooltip.options.defaultClass = 'tooltip';
  VTooltip.options.defaultTemplate = '<div class="tooltip" role="tooltip"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>';
  
  
</script>
  

<style scoped>
  .heatmap {
    /* SVG 스타일 */
  }
  
  .tooltip {
    /* 툴팁 스타일 */
  }
  
</style>
  