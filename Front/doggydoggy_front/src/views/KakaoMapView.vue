<template>
  <div class="map-wrap">
    <!--
      지도 API는 별도의 JavaScript 파일이나 스니펫에 표시되지 않은 스크립트 태그 내에서
      초기화되는 경우가 많고 지도를 표시하는 initMap()에서도
      document.getElementById('map')
      과 같이 div id 를 map으로 지정해야 오류가 발생하지 않음
    -->
    <div id="map" style="width:100%;height:500px;"></div>
    <!-- if="isModalOpen" 조건은 isModalOpen 변수의 값에 따라 모달이 표시되거나 숨겨짐을 결정 -->
    <MarkerModal
        v-if="isModalOpen"
        :latitude="latitude"
        :longitude="longitude"
        :close="closeModal"
        @marker-added="addNewMarkerToMap" />
    <button class="create-marker-btn" @click="openModalForCreatingMarker">마커 생성</button>

    <DestinationModal
        v-if="isDestinationModalOpen"
        :latitude="latitude"
        :longitude="longitude"
        :close="closeModal"
        @destination-confirmed="handleDestinationConfirmed(d_marker)"
    />


  </div>


</template>

<script setup>
import { onMounted, ref, reactive } from 'vue';
import MarkerModal from './MarkerModal.vue'; // 모달 컴포넌트 가져오기
import DestinationModal from './DestinationModal.vue';
import axios from 'axios';

const isModalOpen = ref(false); // 모달 상태 관리
const isDestinationModalOpen = ref(false);
const latitude = ref(null); // 위도
const longitude = ref(null); // 경도
const markers = ref([]); // 마커 배열
const userLocation = reactive({ latitude: null, longitude: null }); // 사용자 위치
let map = null; // 지도 객체
let clickMarker = null; // 클릭 마커
let ps = null;
let clusterer = null; // 클러스터러 객체 추가
let currentOpenOverlay = null;
let previousOpenOverlay = null;
let currentPolyline = null;

let d_marker = ref(null);

// ----- 경로 그리기-----






//--------------

//Geolocation API를 사용하여 사용자의 현재 위치(위도와 경도)를 가져옴 // PWA
function getUserLocationAndInitMap() {
  navigator.geolocation.getCurrentPosition(
      (pos) => {
        // 위치 정보 성공적으로 가져옴
        userLocation.latitude = pos.coords.latitude;
        userLocation.longitude = pos.coords.longitude;
        initMap(); // 지도 초기화 호출
      },
      () => {
        initMap(); // 위치 정보를 가져오지 못한 경우 기본 설정으로 지도 초기화
      }
  );
}

onMounted(() => {
  loadKakaoMapsAPI();
  displayStoredMarkers();
});

// API 불러오는 함수 데이터 없음
function loadKakaoMapsAPI() {
  // window 객체에서 kakao와 kakao.maps가 이미 존재하는지를 확인
  if (!window.kakao || !window.kakao.maps) {
    // 새로운 <script> 태그를 생성
    const script = document.createElement('script');
    // 생성된 스크립트의 타입을 JavaScript로 설정
    script.type = 'text/javascript';
    script.src = "//dapi.kakao.com/v2/maps/sdk.js?appkey=YOUR_APP_KEY&libraries=services,clusterer";
    // load 이벤트가 발생할 때 initMap() 함수를 호출
    script.addEventListener('load', function() {
      initMap();
    });
    // 생성한 스크립트 요소를 문서의 <head> 부분에 추가
    document.head.appendChild(script);
  } else {
    //  API가 이미 로드되어 있다면, 바로 initMap 함수를 호출 ,
    initMap(); // 지도 그리는 함수
  }
}

// 처음 웹을 시작 할 때 로컬 스토리지 마커 데이터를 순회하면서 마커 및 오버레이 생성
// GET
function displayStoredMarkers() {
  // localStorage에서 'markers'라는 키로 저장된 마커 데이터를 가져옴
  // parse => JSON text를 JavaScript 객체로 변환
  // 저장된 데이터가 없다면 빈 배열([])을 사용
  const storedMarkers = JSON.parse(localStorage.getItem('markers')) || [];

  storedMarkers.forEach(markerData => {
    addMarkerToMap(markerData);
    console.log(markerData)
  });
}

// 지도 그리는 함수
// 초기 맵 그리려는 위치 받아오기??
function initMap() {
  const container = document.getElementById('map');
  const options = {
    center: new kakao.maps.LatLng(36.32882145702877, 127.40634269304897), // 초기 맵 그리는 위치
    level: 5
  };

  map = new kakao.maps.Map(container, options);
  ps = new kakao.maps.services.Places();

  clusterer = new kakao.maps.MarkerClusterer({
    map: map,
    averageCenter: true,
    minLevel: 1,
    disableClickZoom: true,

    calculator: [3, 5, 8], // 클러스터의 크기 구분 값 (미만), 각 사이값마다 설정된 text나 style이 적용된다
    texts: getTexts, // texts는 ['삐약', '꼬꼬', '꼬끼오', '치멘'] 이렇게 배열로도 설정할 수 있다
    styles: [{ // calculator 각 사이 값 마다 적용될 스타일을 지정한다
      width : '30px', height : '30px',
      background: 'rgba(51, 204, 255, .8)',
      borderRadius: '15px',
      color: '#000',
      textAlign: 'center',
      fontWeight: 'bold',
      lineHeight: '31px'
    },
      {
        width : '40px', height : '40px',
        background: 'rgba(255, 153, 0, .8)',
        borderRadius: '20px',
        color: '#000',
        textAlign: 'center',
        fontWeight: 'bold',
        lineHeight: '41px'
      },
      {
        width : '50px', height : '50px',
        background: 'rgba(255, 51, 204, .8)',
        borderRadius: '25px',
        color: '#000',
        textAlign: 'center',
        fontWeight: 'bold',
        lineHeight: '51px'
      },
      {
        width : '60px', height : '60px',
        background: 'rgba(255, 80, 80, .8)',
        borderRadius: '30px',
        color: '#000',
        textAlign: 'center',
        fontWeight: 'bold',
        lineHeight: '61px'
      }
    ]

    // 클러스터러에 대한 추가 옵션을 여기에 추가
  });

  // 클러스터 오버레이 만드는 함수 // 클러스터된 마커들의 데이터를 합쳐주는 함수
  // markerInfos 받아오기
  function createClusterInfoWindowContent(markerInfos, infoWindow, map) {


    const infoContentDiv = document.createElement('div');
    infoContentDiv.style.cssText = "width: 250px; height: 400px; overflow-y: auto; position: relative;";
    // for문처럼 각각 데이터 돌면서 각 마커마다 찍어주는거
    // 변수 정의 하는 느낌 각각 돌면서
    markerInfos.forEach(info => {
      const markerInfoDiv = document.createElement('div');
      markerInfoDiv.style.marginBottom = '10px'; // 여백 추가

      // 제목
      const titleDiv = document.createElement('div');
      titleDiv.textContent = `${info.title}`;
      titleDiv.style.fontWeight = '600';
      markerInfoDiv.appendChild(titleDiv);

      // 내용
      const contentDiv = document.createElement('div');
      contentDiv.textContent = `${info.content}`;
      markerInfoDiv.appendChild(contentDiv);

      // 위도, 경도
      const latLngDiv = document.createElement('div');
      latLngDiv.textContent = `Latitude: ${info.latitude}, Longitude: ${info.longitude}`;
      markerInfoDiv.appendChild(latLngDiv);

      // 이미지가 있을 경우 이미지 추가
      if (info.image) {
        const image = document.createElement('img');
        image.src = info.image;
        image.style.cssText = 'max-width: 100%; height: auto; margin-top: 5px;';
        markerInfoDiv.appendChild(image);
      }

      const commentsDiv = document.createElement('div');
      commentsDiv.className = 'comments';
      loadComments(info, commentsDiv);
      markerInfoDiv.appendChild(commentsDiv);

    

      // 클러스터 생성에 참조된 마커 정보에 클릭 이벤트 추가
      // 해당 마커 정보에 클릭 시 현재 오버레이를 닫고
      // 해당 마커의 오버레이를 출력

      markerInfoDiv.addEventListener('click', () => {

        if (currentOpenOverlay) {
        currentOpenOverlay.close();
        currentOpenOverlay = null;
        }


        infoWindow.close(); // 클러스터의 인포윈도우를 닫습니다.


        const moveLatLon = new kakao.maps.LatLng(info.latitude, info.longitude);

        const level = 3
    
    // 지도를 1레벨 내립니다 (지도가 확대됩니다)
        map.setLevel(level);
        map.panTo(moveLatLon);


        const overlay = createOverlay(info, info.marker); // 마커의 오버레이를 생성합니다.
        overlay.open(map, info.marker); // 마커의 오버레이를 엽니다.
        currentOpenOverlay = overlay;
      });

      infoContentDiv.appendChild(markerInfoDiv);
    });

    // 닫기 버튼
    const closeButton = document.createElement('button');
    closeButton.textContent = 'X';
    closeButton.className = 'overlay-close-btn';
    closeButton.style.cssText = 'position: absolute; top: 5px; right: 5px;';
    closeButton.addEventListener('click', () => {
      infoWindow.close();
    });
    infoContentDiv.appendChild(closeButton);

    return infoContentDiv;
  }

  // 클러스터 클릭 이벤트 핸들러에서 마커 정보에 이미지 정보를 포함시키는 부분
  kakao.maps.event.addListener(clusterer, 'clusterclick', function(cluster) {
    // 클러스터에 포함된 마커들의 정보를 가져옵니다.
    const markers = cluster.getMarkers();
    const markerInfos = markers.map(marker => {
      return {
        title: marker.customInfo.title,
        content: marker.customInfo.content,
        latitude: marker.customInfo.latitude,
        longitude: marker.customInfo.longitude,
        image: marker.customInfo.image, // 이미지 정보 추가
        comments: marker.customInfo.comments,
        marker: marker
      };
    });

    if (currentOpenOverlay) {
    currentOpenOverlay.close();
    currentOpenOverlay = null;
    }

    // 인포윈도우 생성 및 표시
    const infoWindow = new kakao.maps.InfoWindow({
      position: cluster.getCenter()
    });
    const infoWindowContent = createClusterInfoWindowContent(markerInfos, infoWindow, map);
    infoWindow.setContent(infoWindowContent);
    infoWindow.open(map);
    currentOpenOverlay = infoWindow;
  });

  // 클릭 이벤트 리스너 추가
  // .addListener() = > 첫 번째 인수 - 대상 개체(map) , 두 번째 인수 - 이벤트 유형('클릭'), 세 번째 인수 - 콜백 함수:지도를 클릭할 때 호출되는 함수
  // function(mouseEvent) ->마우스 이벤트 변수를 받아서 현재의 위치를 객체에 담음 - 즉 지도를 클릭하면 마지막 파라미터로 넘어온 함수를 호출
  kakao.maps.event.addListener(map, 'click', function(mouseEvent) {
    onMapClick(mouseEvent);
    DestinationClick(mouseEvent);
  });

}

// displayStoredMarkers() 랑 기능은 거의 비슷한데 displayStoredMarkers()는 웹 시작 때 동작하는 함수라
// 마커 생성하는 즉시 지도에 마커를 띄우고 그 마커에 오버레이를 추가하기 위해 따로 함수 생성
function addMarkerToMap(markerData) {

  const position = new kakao.maps.LatLng(markerData.latitude, markerData.longitude);
  const marker = new kakao.maps.Marker({
    map: null,
    position: position,
    title: markerData.title,
  });

  marker.customInfo = {
    title: markerData.title,
    content: markerData.content,
    latitude: markerData.latitude,
    longitude: markerData.longitude,
    image: markerData.image,
  };
  // 오버레이 추가
  const overlay = createOverlay(markerData, marker);


  kakao.maps.event.addListener(marker, 'click', function() {
// 이미 열려 있는 오버레이가 있다면, 그 오버레이를 닫음
    if (currentOpenOverlay) {
      currentOpenOverlay.close();
    }
    // 클릭된 마커에 연결된 오버레이를 지도에 표시
    overlay.open(map, marker);
    // 현재 열린 오버레이를 currentOpenOverlay 변수에 저장
    currentOpenOverlay = overlay;
  });

  // 클러스터를 반응형으로 할려고 마커 생성 시 클러스터에 현재 생성 마커 값 집어넣음
  clusterer.addMarker(marker);
}

// ---------------오버레이 관련 코드------------

function createOverlay(markerData, marker) {


  const overlayDiv = document.createElement('div');
  overlayDiv.style.cssText = "width: 250px; height: 400px; overflow-y: auto; position: relative;";

  // Add title
  const titleDiv = document.createElement('div');
  titleDiv.textContent = markerData.title;
  titleDiv.style.fontWeight = '600';
  overlayDiv.appendChild(titleDiv);

  // Add latitude
  const latitudeDiv = document.createElement('div');
  latitudeDiv.textContent = `Latitude: ${markerData.latitude}`;
  latitudeDiv.style.fontWeight = '600';
  overlayDiv.appendChild(latitudeDiv);

  // Add longitude
  const longitudeDiv = document.createElement('div');
  longitudeDiv.textContent = `Longitude: ${markerData.longitude}`;
  longitudeDiv.style.fontWeight = '600';
  overlayDiv.appendChild(longitudeDiv);


  // 댓글 리스트 표시를 위한 Div 추가
  const commentsDiv = document.createElement('div');
  commentsDiv.className = 'comments';
  overlayDiv.appendChild(commentsDiv);

  // 댓글 입력 필드 추가
  const commentInput = document.createElement('input');
  commentInput.type = 'text';
  commentInput.placeholder = '댓글을 입력하세요';
  overlayDiv.appendChild(commentInput);

  // 댓글 생성 버튼 추가
  const commentButton = document.createElement('button');
  commentButton.textContent = '댓글 생성';
  commentButton.addEventListener('click', () => {
    addComment(markerData, commentInput.value);
    commentInput.value = '';
    // 댓글을 로드하고 표시하는 함수
    loadComments(markerData, commentsDiv);

  });
  overlayDiv.appendChild(commentButton);

  loadComments(markerData, commentsDiv);


  // Add image if it exists
  if (markerData.image) {
    const image = document.createElement('img');
    image.src = markerData.image;
    image.style.cssText = 'max-width: 100%; height: auto;';
    overlayDiv.appendChild(image);
  }

  // Add content
  const contentDiv = document.createElement('div');
  contentDiv.textContent = markerData.content;
  contentDiv.style.fontWeight = '600';
  overlayDiv.appendChild(contentDiv);

  // Create and add close button
  const closeButton = document.createElement('button');
  closeButton.textContent = 'X';
  closeButton.className = 'overlay-close-btn';
  closeButton.style.cssText = 'position: absolute; top: 5px; right: 5px;';
  closeButton.addEventListener('click', () => {
    infoWindow.close();
    currentOpenOverlay = null;
  });
  overlayDiv.appendChild(closeButton);


  const switchButton = document.createElement('button');
  switchButton.textContent = '산책 게시판';
  switchButton.addEventListener('click', () => {
    if (currentOpenOverlay) {
      currentOpenOverlay.close(); // 현재 열린 오버레이 닫기
    }
    const newOverlay = createNewOverlay(markerData, marker);

    newOverlay.open(map, marker);
    currentOpenOverlay = newOverlay; // 현재 열린 오버레이 업데이트
    console.log('산책게시판',currentOpenOverlay)
  });
  overlayDiv.appendChild(switchButton);




  const deleteButton = document.createElement('button');
  deleteButton.textContent = 'Delete Marker';
  deleteButton.className = 'overlay-delete-btn';
  deleteButton.addEventListener('click', () => {
    // 마커와 오버레이 삭제 처리
    deleteMarker(markerData, marker);
  });
  overlayDiv.appendChild(deleteButton);


  const position = new kakao.maps.LatLng(markerData.latitude, markerData.longitude);

  // Create InfoWindow with the overlayDiv as content
  const infoWindow = new kakao.maps.InfoWindow({
    position: position,
    content: overlayDiv
  });

  return infoWindow;
}


// -------------카카오 모빌리리토 길 찾기 경로값을 얻어오고 그걸 통해 선을 그리는 것

async function getCarDirection(map, postLat, postLng, markerLat, markerLng) {
  const REST_API_KEY = 'f263f4cccf14d7c0b53113b856a963a5';
  const url = 'https://apis-navi.kakaomobility.com/v1/directions';

  const origin = `${markerLng},${markerLat}`;
  const destination = `${postLng},${postLat}`;

  try {
    const response = await axios.get(url, {
      params: { origin, destination },
      headers: { Authorization: `KakaoAK ${REST_API_KEY}`, 'Content-Type': 'application/json' }
    });

    const data = response.data;

    const linePath = data.routes[0].sections[0].roads.reduce((path, router) => {
      router.vertexes.forEach((vertex, index) => {
        if (index % 2 === 0) {
          path.push(new kakao.maps.LatLng(router.vertexes[index + 1], router.vertexes[index]));
        }
      });
      return path;
    }, []);

    if (currentPolyline) {
      currentPolyline.setMap(null); // 이전 폴리라인 제거
    }

    currentPolyline = new kakao.maps.Polyline({
      path: linePath,
      strokeWeight: 5,
      strokeColor: '#000003',
      strokeOpacity: 0.7,
      strokeStyle: 'solid'
    });

    currentPolyline.setMap(map);
  } catch (error) {
    console.error('Error in getCarDirection:', error);
  }
}


//-----------------------





// 산책 게시판 오버레이
function createNewOverlay(markerData, marker) {
  console.log('post',currentOpenOverlay)
  const newOverlayDiv = document.createElement('div');
  newOverlayDiv.style.cssText = "width: 250px; height: 400px; overflow-y: auto; position: relative;";

  const postsListDiv = document.createElement('div');
  postsListDiv.style.cssText = "margin-top: 10px;";

  if (markerData.posts && markerData.posts.length > 0) {
    markerData.posts.forEach(post => {
      const postTitleDiv = document.createElement('div');
      postTitleDiv.textContent = `제목: ${post.postTitle}`;
      postTitleDiv.style.fontWeight = 'bold';

      const postDateDiv = document.createElement('div');
      postDateDiv.textContent = `날짜: ${post.postdate}`;
      postDateDiv.style.marginBottom = '5px'; // 적당한 여백을 제공합니다.

      const postTimeDiv = document.createElement('div');
      postTimeDiv.textContent = `시간: ${post.posttime}`;
      postTimeDiv.style.marginBottom = '10px'; // 적당한 여백을 제공합니다.


      const routeButton = document.createElement('button');
      routeButton.textContent = '경로 보기';
      routeButton.addEventListener('click', () => {
        // 경로 그리기 함수 호출
        getCarDirection(map, post.postlatitude, post.postlongitude, markerData.latitude, markerData.longitude);
      });


      // Add Delete Button
      const deleteButton = document.createElement('button');
      deleteButton.textContent = 'Delete';
      deleteButton.addEventListener('click', () => deletePost(post.id, markerData, marker));


      postsListDiv.appendChild(postTitleDiv);
      postsListDiv.appendChild(postDateDiv);
      postsListDiv.appendChild(postTimeDiv);
      postsListDiv.appendChild(routeButton);
      postsListDiv.appendChild(deleteButton);
    });
  } else {
    const noPostsDiv = document.createElement('div');
    noPostsDiv.textContent = '게시글이 없습니다.';
    postsListDiv.appendChild(noPostsDiv);
  }

  newOverlayDiv.appendChild(postsListDiv);

  // Create and add close button
  const closeButton = document.createElement('button');
  closeButton.textContent = 'X';
  closeButton.className = 'overlay-close-btn';
  closeButton.style.cssText = 'position: absolute; top: 5px; right: 5px;';
  closeButton.addEventListener('click', () => {
    newInfoWindow.close();
    currentOpenOverlay = null;
  });
  newOverlayDiv.appendChild(closeButton);


  const ResetButton = document.createElement('button');
  ResetButton.textContent = '경로 초기화';
  ResetButton.addEventListener('click', () => {
    if (currentPolyline) {
      currentPolyline.setMap(null); // 지도에서 폴리라인 제거
      currentPolyline = null; // 폴리라인 참조 초기화
    }
  });
  newOverlayDiv.appendChild(ResetButton);

  const switchButton = document.createElement('button');
  switchButton.textContent = '이전 오버레이 보기';
  switchButton.addEventListener('click', () => {
    if (currentOpenOverlay) {
      currentOpenOverlay.close(); // 현재 열린 오버레이 닫기
    }
    const originalOverlay = createOverlay(markerData, marker);
    originalOverlay.open(map, marker); // 첫 번째 오버레이 다시 열기(마커 위에 오버레이 띄우는 메서드)
    currentOpenOverlay = originalOverlay; // 현재 열린 오버레이 업데이트
  });
  newOverlayDiv.appendChild(switchButton);


  const postButton = document.createElement('button');
  postButton.textContent = '게시글 생성';
  postButton.addEventListener('click', () => {
    if (currentOpenOverlay) {
      currentOpenOverlay.close(); // 현재 열린 오버레이 닫기
    }
    currentOpenOverlay = createPostOverlay(markerData, marker);
    console.log('click', currentOpenOverlay);
    currentOpenOverlay.open(map, marker);
  });
  newOverlayDiv.appendChild(postButton);

  const position = new kakao.maps.LatLng(markerData.latitude, markerData.longitude);
  // 기존 오버레이 닫기 (필요한 경우)
  if (currentOpenOverlay) {
    currentOpenOverlay.close();
  }

  // Create InfoWindow with the overlayDiv as content
  const newInfoWindow = new kakao.maps.InfoWindow({
    position: position, // Set the position using the LatLng object
    content: newOverlayDiv
  });
  return newInfoWindow;
}

// 게시글 생성 오버레이

function createPostOverlay(markerData, marker) {
  const newOverlayDiv = document.createElement('div');
  newOverlayDiv.style.cssText = "width: 250px; height: 400px; overflow-y: auto; position: relative;";

  const titleInput = document.createElement('input');
  titleInput.placeholder = '제목';
  newOverlayDiv.appendChild(titleInput);

  // 날짜 입력 필드 생성
  const dateInput = document.createElement('input');
  dateInput.type = 'date'; // 입력 필드 유형을 날짜 선택으로 설정
  dateInput.name = 'date'; // 필드의 이름 설정
  dateInput.style.cssText = 'margin-bottom: 10px;'; // 스타일 설정
  newOverlayDiv.appendChild(dateInput); // 생성한 날짜 입력 필드를 오버레이에 추가

  // 시간 입력 필드 생성
  const timeInput = document.createElement('input');
  timeInput.type = 'time'; // 입력 필드 유형을 시간 선택으로 설정
  timeInput.name = 'time'; // 필드의 이름 설정
  timeInput.style.cssText = 'margin-bottom: 10px;'; // 스타일 설정
  newOverlayDiv.appendChild(timeInput); // 생성한 시간 입력 필드를 오버레이에 추가

  // 도착 지점 설정
  const destinationButton = document.createElement('button');
  destinationButton.textContent = '도착지점 설정';

  destinationButton.addEventListener('click', () => {
    openDestinationModal();

    d_marker = marker

    if (currentOpenOverlay) {
      previousOpenOverlay = currentOpenOverlay;
      currentOpenOverlay.close();
      currentOpenOverlay = null;
    }
  });

  newOverlayDiv.appendChild(destinationButton);


  // 완료 버튼
  const submitButton = document.createElement('button');
  submitButton.textContent = '완료';
  submitButton.addEventListener('click', () => {
    const newPost = {
      postTitle: titleInput.value,
      postdate: dateInput.value,
      posttime: timeInput.value,
      postlatitude: latitude.value,
      postlongitude: longitude.value,
    };

    // 마커 데이터 내의 posts 배열에 새 게시글 추가
    if (!markerData.posts) {
      markerData.posts = []; // posts 배열이 없으면 새로 생성
    }
    markerData.posts.push(newPost);

    // 로컬 스토리지 업데이트
    updateLocalStorage(markerData);

    // 오버레이 닫기 및 기존 오버레이로 돌아가기
    const backOverlay2 = createNewOverlay(markerData, marker);
    backOverlay2.open(map, marker); // 첫 번째 오버레이 다시 열기(마커 위에 오버레이 띄우는 메서드)
    currentOpenOverlay = backOverlay2; // 현재 열린 오버레이 업데이트
  });
  newOverlayDiv.appendChild(submitButton);


  // Create and add close button
  const closeButton = document.createElement('button');
  closeButton.textContent = 'X';
  closeButton.className = 'overlay-close-btn';
  closeButton.style.cssText = 'position: absolute; top: 5px; right: 5px;';
  closeButton.addEventListener('click', () => {
    newInfoWindow.close();
    currentOpenOverlay = null;
  });
  newOverlayDiv.appendChild(closeButton);


  const switchButton = document.createElement('button');
  switchButton.textContent = '뒤로가기';
  switchButton.addEventListener('click', () => {
    if (currentOpenOverlay) {
      currentOpenOverlay.close(); // 현재 열린 오버레이 닫기
    }
    const backOverlay = createNewOverlay(markerData, marker);
    backOverlay.open(map, marker); // 첫 번째 오버레이 다시 열기(마커 위에 오버레이 띄우는 메서드)
    currentOpenOverlay = backOverlay; // 현재 열린 오버레이 업데이트
  });
  newOverlayDiv.appendChild(switchButton);


  const position = new kakao.maps.LatLng(markerData.latitude, markerData.longitude);
  // 기존 오버레이 닫기 (필요한 경우)
  if (currentOpenOverlay) {
    currentOpenOverlay.close();
  }

  // Create InfoWindow with the overlayDiv as content
  const newInfoWindow = new kakao.maps.InfoWindow({
    position: position, // Set the position using the LatLng object
    content: newOverlayDiv
  });
  return newInfoWindow;
}


function updateLocalStorage(updatedMarkerData) {
  const storedMarkers = JSON.parse(localStorage.getItem('markers')) || [];
  const markerIndex = storedMarkers.findIndex(m => m.latitude === updatedMarkerData.latitude && m.longitude === updatedMarkerData.longitude);

  if (markerIndex !== -1) {
    // Update existing marker data
    storedMarkers[markerIndex] = updatedMarkerData;
  } else {
    // If marker data does not exist, add new one
    storedMarkers.push(updatedMarkerData);
  }

  // Save the modified marker array to local storage
  localStorage.setItem('markers', JSON.stringify(storedMarkers));
}


//----------마커 생성 모달과 관련된 코드-----












// ----------------CRUD 기능에 관한 함수들

// MarkerModal 컴포넌트에서 마커가 추가된 후 호출될 이벤트 핸들러
function addNewMarkerToMap(newMarker) {
  addMarkerToMap(newMarker);
  // 필요한 경우 여기에서 추가적인 로직을 수행
}


function DestinationClick(mouseEvent) {
  if (!isDestinationModalOpen.value) return;

  const latlng = mouseEvent.latLng;
  latitude.value = latlng.getLat();
  longitude.value = latlng.getLng();
  console.log(latitude.value)
  console.log(longitude.value )
  addClickMarker(latlng);
}


function openDestinationModal() {
  isDestinationModalOpen.value = true;
}





function handleDestinationConfirmed(marker) {
  if (previousOpenOverlay) {
    previousOpenOverlay.open(map, marker);
    currentOpenOverlay = previousOpenOverlay;
    previousOpenOverlay = null;
  }
  closeModal();
}







function openModalForCreatingMarker() {
  isModalOpen.value = true;
}


function onMapClick(mouseEvent) {
  if (!isModalOpen.value) return; // 모달이 열리지 않았으면 아무 것도 하지 않음

  const latlng = mouseEvent.latLng;
  latitude.value = latlng.getLat();
  longitude.value = latlng.getLng();
  addClickMarker(latlng);
}


// 클릭한 위치에 마커를 추가하는 함수
function addClickMarker(position) {
  // 기존에 클릭 마커가 있으면 제거
  if (clickMarker) {
    clickMarker.setMap(null);
  }

  clickMarker = new kakao.maps.Marker({
    position: position
  });

  clickMarker.setMap(map);
}

function closeModal() {
  if (clickMarker) {
    clickMarker.setMap(null); // 지도에서 클릭 마커 제거
    clickMarker = null; // 참조 제거
  }
  isModalOpen.value = false; // 모달 상태를 닫힘으로 설정
  isDestinationModalOpen.value = false;
}


function getTexts( count ) {

// 한 클러스터 객체가 포함하는 마커의 개수에 따라 다른 텍스트 값을 표시합니다
  if(count < 3) {
    return '삐약';
  } else if(count < 5) {
    return '꼬꼬';
  } else if(count < 8) {
    return '꼬끼오';
  } else {
    return '치멘';
  }
}


function deletePost(postId, markerData, marker) {
  // Logic to delete the post
  // 1. Find the post in the markerData.posts array and remove it
  const postIndex = markerData.posts.findIndex(post => post.id === postId);
  if (postIndex !== -1) {
    markerData.posts.splice(postIndex, 1);
  }

  // 2. Update the localStorage with the new markers array
  updateLocalStorage(markerData);

  // 3. Refresh the overlay or any other UI components that need updating
  refreshOverlay(markerData, marker);
}



function deleteMarker(markerData, marker) {
  // Remove the marker from the map
  marker.setMap(null);

  // Remove the marker from local storage
  const storedMarkers = JSON.parse(localStorage.getItem('markers')) || [];
  const updatedMarkers = storedMarkers.filter(m => m.latitude !== markerData.latitude || m.longitude !== markerData.longitude);
  localStorage.setItem('markers', JSON.stringify(updatedMarkers));

  // Update the markers array
  const index = markers.value.findIndex(m => m.latitude === markerData.latitude && m.longitude === markerData.longitude);
  if (index > -1) {
    markers.value.splice(index, 1);
  } 




  // 마커 삭제 시 클러스터 데이터에도 삭제해야 함 (마커가 하나만 있고 표시되는 클러스터의 일부가 아니더라도 여전히 'clusterer' 개체에 의해 관리
  // 되기 때문에 클러스터에서 마커를 제거하지 않으면 마커 삭제 후에도 지도 레벨에 따라 계속 마커가 표시 될 수 있음)
  // Remove the marker from the clusterer
  clusterer.removeMarker(marker);

  // Close the currently open overlay, if any
  if (currentOpenOverlay) {
    currentOpenOverlay.close();
    currentOpenOverlay = null;
  }
}




function addComment(markerData, commentText) {
  if (!markerData.comments) {
    markerData.comments = [];
  }
  const newComment = {
    id: Date.now(), // 고유 ID 생성
    author: '사용자명',
    text: commentText,
    date: new Date().toISOString()
  };
  markerData.comments.push(newComment);
  updateLocalStorage(markerData);
}



// 댓글 로드 및 표시 함수
function loadComments(markerData, commentsDiv) {
  commentsDiv.innerHTML = '';
  if (markerData.comments) {
    markerData.comments.forEach(comment => {
      const commentEl = document.createElement('div');

      // 댓글 내용
      const commentText = document.createElement('span');
      commentText.textContent = `${comment.author}: ${comment.text} (${new Date(comment.date).toLocaleString()})`;
      commentEl.appendChild(commentText);

      // 수정 버튼
      const editButton = document.createElement('button');
      editButton.textContent = '수정';
      editButton.onclick = () => editComment(comment.id, markerData, commentsDiv);
      commentEl.appendChild(editButton);

      // 삭제 버튼
      const deleteButton = document.createElement('button');
      deleteButton.textContent = '삭제';
      deleteButton.onclick = () => deleteComment(comment.id, markerData, commentsDiv);
      commentEl.appendChild(deleteButton);

      commentsDiv.appendChild(commentEl);
    });
  }
}




function editComment(commentId, markerData, commentsDiv) {
  // 댓글을 수정하는 로직을 여기에 추가
  // 예를 들어, 수정할 댓글 내용을 입력받는 프롬프트를 표시하고,
  // 해당 내용으로 댓글을 업데이트한 후 로컬 스토리지를 업데이트합니다.
}

function deleteComment(commentId, markerData, commentsDiv) {
  const updatedComments = markerData.comments.filter(comment => comment.id !== commentId);
  markerData.comments = updatedComments;
  updateLocalStorage(markerData);
  loadComments(markerData, commentsDiv); // 댓글 다시 로드
}


function refreshOverlay(markerData, marker) {
  if (currentOpenOverlay) {
    currentOpenOverlay.close();
  }
  const newOverlay = createNewOverlay(markerData, marker);
  newOverlay.open(map, marker);
  currentOpenOverlay = newOverlay;
}




</script>

<style scoped>
/* 스타일 정의 */

.custom-overlay {
  width: 300px; /* Adjusted width */
  height: 150px; /* Adjusted height */
  /* Add other styling as required */
}


.map_wrap, .map_wrap * {margin:0;padding:0;font-family:'Malgun Gothic',dotum,'돋움',sans-serif;font-size:12px; font-family: 'Arial', sans-serif;}
.map_wrap a, .map_wrap a:hover, .map_wrap a:active{color:#000;text-decoration: none;}
.map_wrap {
  position: relative;
  width: 80%;
  height: 500px;
  margin: 0 auto; /* 상하 마진을 0으로, 좌우 마진을 auto로 설정 */
}
#menu_wrap {position:absolute;top:0;left:0;bottom:0;width:250px;margin:10px 0 30px 10px;padding:5px;overflow-y:auto;background:rgba(255, 255, 255, 0.7);z-index: 1;font-size:12px;border-radius: 10px; border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1); /* 그림자 추가 */
}
.bg_white {background:#fff;}
#menu_wrap hr {display: block; height: 1px;border: 0; border-top: 2px solid #5F5F5F;margin:3px 0;}
#menu_wrap .option{text-align: center;}
#menu_wrap .option p {margin:10px 0;}
#menu_wrap .option button {margin-left:5px;}
#placesList li {list-style: none;}
#placesList .item {position:relative;border-bottom:1px solid #888;overflow: hidden;cursor: pointer;min-height: 65px;}
#placesList .item span {display: block;margin-top:4px;}
#placesList .item h5, #placesList .item .info {text-overflow: ellipsis;overflow: hidden;white-space: nowrap;}
#placesList .item .info{padding:10px 0 10px 55px;}
#placesList .info .gray {color:#8a8a8a;}
#placesList .info .jibun {padding-left:26px;background:url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/places_jibun.png) no-repeat;}
#placesList .info .tel {color:#009900;}
#placesList .item .markerbg {float:left;position:absolute;width:36px; height:37px;margin:10px 0 0 10px;background:url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png) no-repeat;}
#placesList .item .marker_1 {background-position: 0 -10px;}
#placesList .item .marker_2 {background-position: 0 -56px;}
#placesList .item .marker_3 {background-position: 0 -102px}
#placesList .item .marker_4 {background-position: 0 -148px;}
#placesList .item .marker_5 {background-position: 0 -194px;}
#placesList .item .marker_6 {background-position: 0 -240px;}
#placesList .item .marker_7 {background-position: 0 -286px;}
#placesList .item .marker_8 {background-position: 0 -332px;}
#placesList .item .marker_9 {background-position: 0 -378px;}
#placesList .item .marker_10 {background-position: 0 -423px;}
#placesList .item .marker_11 {background-position: 0 -470px;}
#placesList .item .marker_12 {background-position: 0 -516px;}
#placesList .item .marker_13 {background-position: 0 -562px;}
#placesList .item .marker_14 {background-position: 0 -608px;}
#placesList .item .marker_15 {background-position: 0 -654px;}
#pagination {margin:10px auto;text-align: center;}
#pagination a {display:inline-block;margin-right:10px;}
#pagination .on {font-weight: bold; cursor: default;color:#777;}

.map-title {
  text-align: center; /* 텍스트를 중앙 정렬 */
  font-size: 24px; /* 텍스트 크기 설정 */
  font-weight: bold; /* 폰트 굵기 설정 */
  padding: 20px 0; /* 위아래 패딩 설정 */
  margin-bottom: 20px;
  margin-top: 20px;
  color: #333; /* 텍스트 색상 설정 */

}

.map-title h4 {
  font-size: 18px; /* 폰트 크기 작게 설정 */
  font-weight: normal; /* 폰트 굵기 보통으로 설정 */
  margin-top: 30px; /* 상단 여백 설정 */
  color: #666; /* 텍스트 색상을 조금 더 어둡게 설정 */
}

.close {
  position: absolute;
  top: 5px; /* 버튼의 상단 위치 조정 */
  right: 5px; /* 버튼의 우측 위치 조정 */
  width: 20px; /* 버튼의 너비 */
  height: 20px; /* 버튼의 높이 */
  background: url('닫기 버튼 이미지 URL') no-repeat center/cover; /* 버튼 배경 이미지 */
  cursor: pointer;
}


</style>
