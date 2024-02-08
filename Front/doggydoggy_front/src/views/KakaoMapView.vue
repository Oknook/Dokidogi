<template>
  <div class="map-wrap">
    <!--
      지도 API는 별도의 JavaScript 파일이나 스니펫에 표시되지 않은 스크립트 태그 내에서
      초기화되는 경우가 많고 지도를 표시하는 initMap()에서도
      document.getElementById('map')
      과 같이 div id 를 map으로 지정해야 오류가 발생하지 않음
    -->
    <div id="map" style="width:100%;height:700px;"></div>
    <!-- if="isModalOpen" 조건은 isModalOpen 변수의 값에 따라 모달이 표시되거나 숨겨짐을 결정 -->
    <div class="modal-component">
      <MarkerModal
          v-if="isModalOpen"
          :close="closeModal"
          :latitude="latitude"
          :longitude="longitude"
          @marker-added="addNewMarkerToMap"/>
      <button class="create-marker-btn" @click="openModalForCreatingMarker">마커 생성</button>
    </div>


    <DestinationModal
        v-if="isDestinationModalOpen"
        :close="closeModal"
        :latitude="latitude"
        :longitude="longitude"
        @destination-confirmed="handleDestinationConfirmed(d_marker)"
    />

    <MarkerUpdateModal
        v-if="isUpdateModalOpen"
        :close="closeModal"
        :latitude="latitude"
        :longitude="longitude"
        @marker-added="addNewMarkerToMap"/>


  </div>


</template>

<script setup>
import {onMounted, reactive, ref} from 'vue';
import MarkerModal from './MarkerModal.vue'; // 모달 컴포넌트 가져오기
import DestinationModal from './DestinationModal.vue';
import axios from 'axios';

const isModalOpen = ref(false); // 모달 상태 관리
const isDestinationModalOpen = ref(false);
const latitude = ref(null); // 위도
const longitude = ref(null); // 경도
const markers = ref([]); // 마커 배열
const userLocation = reactive({latitude: null, longitude: null}); // 사용자 위치
let map = null; // 지도 객체
let clickMarker = null; // 클릭 마커
let ps = null;
let clusterer = null; // 클러스터러 객체 추가
let currentOpenOverlay = null;
let previousOpenOverlay = null;
let currentPolyline = null;

let d_marker = ref(null);


const isUpdateModalOpen = ref(false); // 모달 상태 관리
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
  fetchMarkersAndDisplay();
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
    script.addEventListener('load', function () {
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
async function fetchMarkersAndDisplay() {
  try {
    // 서버에서 마커 데이터를 가져옴
    const response = await axios.get('/api/marker/');
    const markers = response.data; // 서버로부터 받은 마커 데이터
    console.log('fetcj', markers)
    // 가져온 마커 데이터로 마커를 지도에 추가
    markers.forEach(markerData => {
      // 수정된 addMarkerToMap 함수를 호출하여 마커를 지도에 추가
      addMarkerToMap({
        id: markerData.markerId,
        latitude: markerData.markerLatitude,
        longitude: markerData.markerLongitude,
        title: markerData.markerTitle,
        content: markerData.markerContents,
        image: markerData.markerStoredFileName, // 서버 응답에 따라 이 부분의 이름이 달라질 수 있음
      });
    });
  } catch (error) {
    console.error('Failed to fetch markers:', error);
  }
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
      width: '30px', height: '30px',
      background: 'rgba(51, 204, 255, .8)',
      borderRadius: '15px',
      color: '#000',
      textAlign: 'center',
      fontWeight: 'bold',
      lineHeight: '31px'
    },
      {
        width: '40px', height: '40px',
        background: 'rgba(255, 153, 0, .8)',
        borderRadius: '20px',
        color: '#000',
        textAlign: 'center',
        fontWeight: 'bold',
        lineHeight: '41px'
      },
      {
        width: '50px', height: '50px',
        background: 'rgba(255, 51, 204, .8)',
        borderRadius: '25px',
        color: '#000',
        textAlign: 'center',
        fontWeight: 'bold',
        lineHeight: '51px'
      },
      {
        width: '60px', height: '60px',
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
        let fullImgUrl = `http://localhost:8080/images/${info.image}`;
        image.src = fullImgUrl;
        // image.src = info.image;
        image.style.cssText = 'max-width: 100%; height: auto; margin-top: 5px;';
        markerInfoDiv.appendChild(image);
      }

      const commentsDiv = document.createElement('div');
      commentsDiv.className = 'comments';
      console.log('클러스터', info)
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
  kakao.maps.event.addListener(clusterer, 'clusterclick', function (cluster) {
    // 클러스터에 포함된 마커들의 정보를 가져옵니다.
    const markers = cluster.getMarkers();
    const markerInfos = markers.map(marker => {
      return {
        id: marker.customInfo.id,
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
  kakao.maps.event.addListener(map, 'click', function (mouseEvent) {
    onMapClick(mouseEvent);
    DestinationClick(mouseEvent);
  });

}

// displayStoredMarkers() 랑 기능은 거의 비슷한데 displayStoredMarkers()는 웹 시작 때 동작하는 함수라
// 마커 생성하는 즉시 지도에 마커를 띄우고 그 마커에 오버레이를 추가하기 위해 따로 함수 생성
function addMarkerToMap(markerData) {
  console.log((markerData))
  const position = new kakao.maps.LatLng(markerData.latitude, markerData.longitude);
  const marker = new kakao.maps.Marker({
    map: null,
    position: position,
    title: markerData.title,
  });
// 마커 데이터 받아오는거 수정
  marker.customInfo = {
    id: markerData.id,
    title: markerData.title,
    content: markerData.content,
    latitude: markerData.latitude,
    longitude: markerData.longitude,
    image: markerData.image,

  };
  // 오버레이 추가


  kakao.maps.event.addListener(marker, 'click', async function () {
    // If there is already an overlay open, close that overlay
    if (currentOpenOverlay) {
      currentOpenOverlay.close();
    }

    // Before opening the overlay, fetch the latest comments
    // Assuming `loadComments` is now an async function that returns the latest comments
    const overlay = createOverlay(markerData, marker);

    // Display the overlay connected to the clicked marker on the map
    overlay.open(map, marker);

    // Save the currently open overlay to the currentOpenOverlay variable
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

  // Add image if it exists
  // 사진 값 넣기
  if (markerData.image) {
    const image = document.createElement('img');
    let fullImgUrl = `http://localhost:8080/images/${markerData.image}`;
    image.src = fullImgUrl;
    image.style.cssText = 'max-width: 100%; height: auto;';
    overlayDiv.appendChild(image);
  }

  // Add content
  const contentDiv = document.createElement('div');
  contentDiv.textContent = markerData.content;
  contentDiv.style.fontWeight = '600';
  overlayDiv.appendChild(contentDiv);

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
    console.log('오버레이 댓글 마커데이터', markerData)
    loadComments(markerData, commentsDiv);

  });
  overlayDiv.appendChild(commentButton);

  loadComments(markerData, commentsDiv);

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
  switchButton.addEventListener('click', async () => { // async 키워드 추가
    if (currentOpenOverlay) {
      currentOpenOverlay.close(); // 현재 열린 오버레이 닫기
    }
    const newOverlay = await createNewOverlay(markerData, marker);
    newOverlay.open(map, marker);
    currentOpenOverlay = newOverlay; // 현재 열린 오버레이 업데이트
    console.log('산책게시판', currentOpenOverlay)
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


  // 마커 수정 버튼 추가
  const editButton = document.createElement('button');
  editButton.textContent = 'Edit Marker';
  editButton.className = 'overlay-edit-btn';
  editButton.addEventListener('click', () => {
    openUpdateModal()

  });
  overlayDiv.appendChild(editButton);


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
      params: {origin, destination},
      headers: {Authorization: `KakaoAK ${REST_API_KEY}`, 'Content-Type': 'application/json'}
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
async function createNewOverlay(markerData, marker) {
  console.log('post', currentOpenOverlay)
  const newOverlayDiv = document.createElement('div');
  newOverlayDiv.style.cssText = "width: 250px; height: 400px; overflow-y: auto; position: relative;";

  const postsListDiv = document.createElement('div');
  postsListDiv.style.cssText = "margin-top: 10px;";

  try {
    // axios를 사용하여 서버로부터 산책 게시글 데이터 가져오기
    const response = await axios.get(`/api/walkPost/${markerData.id}`);
    const posts = response.data;
    console.log('산책2', response)
    console.log('산책', posts)

    if (posts && posts.length > 0) {
      posts.forEach(post => {
        // 게시글 정보를 기반으로 UI 요소 생성
        const postTitleDiv = document.createElement('div');
        postTitleDiv.textContent = `제목: ${post.walkPostTitle}`;
        postTitleDiv.style.fontWeight = 'bold';

        const postDateDiv = document.createElement('div');
        postDateDiv.textContent = `날짜: ${post.walkPostDate}`;
        postDateDiv.style.marginBottom = '5px';

        const postTimeDiv = document.createElement('div');
        postTimeDiv.textContent = `시간: ${post.walkPostTime}`;
        postTimeDiv.style.marginBottom = '10px';

        const routeButton = document.createElement('button');
        routeButton.textContent = '경로 보기';
        routeButton.addEventListener('click', () => {
          // 경로 그리기 함수 호출
          getCarDirection(map, post.walkEnd, post.walkStart, markerData.latitude, markerData.longitude);
        });

        // 삭제 버튼
        const deleteButton = document.createElement('button');
        deleteButton.textContent = 'Delete';
        deleteButton.addEventListener('click', () => deletePost(post.walkPostId, markerData, marker));

        // 수정 버튼
        const postUpdateButton = document.createElement('button');
        postUpdateButton.textContent = '게시글 수정';
        postUpdateButton.addEventListener('click', () => {
          if (currentOpenOverlay) {
            currentOpenOverlay.close(); // 현재 열린 오버레이 닫기
          }
          currentOpenOverlay = createUpdatePostOverlay(markerData, marker);
          console.log('click', currentOpenOverlay);
          currentOpenOverlay.open(map, marker);
        });
        postsListDiv.appendChild(postUpdateButton);


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
  } catch (error) {
    console.error("산책 게시글 조회 중 오류 발생:", error);
    // 오류 처리 로직
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
  submitButton.addEventListener('click', async () => { // async 키워드 추가
    const newPost = {
      walkPostTitle: titleInput.value,
      walkPostDate: dateInput.value,
      walkPostTime: timeInput.value,
      walkStart: latitude.value, // 서버 측 DTO 구조에 맞게 필드명을 조정하세요
      walkEnd: longitude.value, // 서버 측 DTO 구조에 맞게 필드명을 조정하세요
    };

    console.log(newPost)

    // Axios를 사용하여 서버로 데이터 전송
    try {
      const response = await axios.post(`/api/walkPost/${markerData.id}/save`, newPost);
      console.log(response.data); // Response logging
      alert("The walking post was created successfully.");

      // Assuming createNewOverlay returns a promise that resolves to an overlay
      const backOverlay2 = await createNewOverlay(markerData, marker); // Await this call
      backOverlay2.open(map, marker);
      currentOpenOverlay = backOverlay2;
    } catch (error) {
      console.error("An error occurred while creating a walk post:", error);
      alert("Failed to create a walking post.");
    }
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


function createUpdatePostOverlay(markerData, marker) {
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
  submitButton.addEventListener('click', async () => { // async 키워드 추가
    const newPost = {
      walkPostTitle: titleInput.value,
      walkPostDate: dateInput.value,
      walkPostTime: timeInput.value,
      walkStart: latitude.value, // 서버 측 DTO 구조에 맞게 필드명을 조정하세요
      walkEnd: longitude.value, // 서버 측 DTO 구조에 맞게 필드명을 조정하세요
    };

    console.log(newPost)

    // Axios를 사용하여 서버로 데이터 전송
    try {
      const response = await axios.put(`/api/walkPost/${markerData.id}/${post.walkPostId}/update`, updatedPost);
      console.log('Post updated successfully:', response.data);
      alert("The walking post was updated successfully.");

      // If you want to refresh the overlay to show updated information
      // you might want to call a function here that refreshes or closes and reopens the overlay
      // For example: refreshOverlay(markerData, marker);
    } catch (error) {
      console.error("An error occurred while updating the walk post:", error);
      alert("Failed to update the walking post.");
    }
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
  console.log('newmarker', newMarker)

  addMarkerToMap({
    id: newMarker.markerId,
    latitude: newMarker.markerLatitude,
    longitude: newMarker.markerLongitude,
    title: newMarker.markerTitle,
    content: newMarker.markerContents,
    image: newMarker.markerStoredFileName, // 서버 응답에 따라 이 부분의 이름이 달라질 수 있음
  });
  // 마커를 지도에 직접 추가
  // 클러스터러에 마커 추가 (필요한 경우)
  clusterer.addMarker(new kakao.maps.Marker({
    position: new kakao.maps.LatLng(newMarker.latitude, newMarker.longitude),
    title: newMarker.title
  }));
}


function DestinationClick(mouseEvent) {
  if (!isDestinationModalOpen.value) return;

  const latlng = mouseEvent.latLng;
  latitude.value = latlng.getLat();
  longitude.value = latlng.getLng();
  console.log(latitude.value)
  console.log(longitude.value)
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


function openUpdateModal() {
  isUpdateModalOpen.value = true;
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


function getTexts(count) {

// 한 클러스터 객체가 포함하는 마커의 개수에 따라 다른 텍스트 값을 표시합니다
  if (count < 3) {
    return '삐약';
  } else if (count < 5) {
    return '꼬꼬';
  } else if (count < 8) {
    return '꼬끼오';
  } else {
    return '치멘';
  }
}


async function deletePost(walkPostId, markerData, marker) {
  try {
    const response = await axios.delete(`/api/walkPost/${markerData.id}/${walkPostId}/delete`);
    if (response.status === 200) {
      // 성공적으로 삭제되었을 때의 로직
      // 예: UI에서 해당 게시글을 제거하거나, 사용자에게 성공 메시지 표시
      console.log('Post deleted successfully');
      refreshOverlay(markerData, marker)
    }
  } catch (error) {
    console.error('Failed to delete the post:', error);
    // 오류 처리 로직, 예: 사용자에게 실패 메시지 표시
  }
}

async function deleteMarker(markerData, marker) {
  try {
    // 산책 게시판 게시글이 있는지 확인합니다.
    const response = await axios.get(`/api/walkPost/${markerData.id}`);
    const posts = response.data;

    // 게시글이 존재하는 경우, 사용자에게 경고 메시지를 표시하고 삭제를 중단합니다.
    if (posts.length > 0) {
      alert("이 마커와 관련된 산책 게시판의 게시글이 존재합니다. 마커를 삭제하려면 먼저 게시글을 삭제하세요.");
      return; // 함수 실행을 여기서 중단합니다.
    }

    // 게시글이 없는 경우, 마커 삭제 요청을 서버에 보냅니다.
    const deleteResponse = await axios.delete(`/api/marker/${markerData.id}/delete`);
    if (deleteResponse.status === 200) {
      // 요청이 성공했다면, 지도에서 마커를 제거합니다.
      marker.setMap(null);
      // 클러스터러에서 마커를 제거합니다.
      clusterer.removeMarker(marker);
      // 현재 열린 오버레이가 있다면 닫습니다.
      if (currentOpenOverlay) {
        currentOpenOverlay.close();
        currentOpenOverlay = null;
      }
      console.log('Marker deleted successfully');
    } else {
      console.error('Failed to delete the marker:', deleteResponse.status);
    }
  } catch (error) {
    console.error('Error deleting marker or checking posts:', error);
  }
}


async function addComment(markerData, commentText) {
  try {
    const newComment = {
      markerCommentContents: commentText,
      // 필요한 경우 여기에 다른 필드 추가
    };

    await axios.post(`/api/marker/${markerData.id}/markerComment/save`, newComment);
    // 댓글을 성공적으로 추가한 후, 댓글 목록을 다시 로드
    loadComments(markerData, document.querySelector('.comments')); // 적절한 선택자로 변경 필요

  } catch (error) {
    console.error('Failed to add comment:', error);
  }
}


// 댓글 로드 및 표시 함수
// 댓글 로드 및 표시 함수
// 댓글 로드 및 표시 함수
async function loadComments(markerData, commentsDiv) {
  try {
    // Axios를 사용하여 댓글 목록을 조회하는 GET 요청
    const response = await axios.get(`/api/marker/${markerData.id}/markerComment`);
    const comments = response.data; // 서버로부터 받은 댓글 데이터

    // 댓글 표시 요소 초기화
    commentsDiv.innerHTML = '';

    // 받아온 댓글 데이터로 댓글을 표시
    comments.forEach(comment => {
      const commentElement = document.createElement('div');
      const commentText = document.createTextNode(`${comment.markerCommentWriter}: ${comment.markerCommentContents}`);
      commentElement.appendChild(commentText);


      const editButton = document.createElement('button');
      editButton.textContent = '수정';
      editButton.onclick = async () => {
        // 사용자로부터 새로운 댓글 내용을 입력받습니다.
        const newComment = prompt("댓글을 수정하세요", comment.markerCommentContents);
        if (newComment !== null && newComment !== comment.markerCommentContents) {
          try {
            await axios.put(`/api/marker/${markerData.id}/markerComment/${comment.markerCommentId}/update`, {
              ...comment,
              markerCommentContents: newComment
            });
            // 성공적으로 수정되면, 댓글 목록을 다시 로드
            loadComments(markerData, commentsDiv);
          } catch (error) {
            console.error('Failed to edit comment:', error);
          }
        }
      };
      commentElement.appendChild(editButton);


      // 삭제 버튼 추가
      const deleteButton = document.createElement('button');
      deleteButton.textContent = '삭제';
      deleteButton.onclick = async () => {
        // 댓글 삭제 API 호출
        try {
          await axios.delete(`/api/marker/${markerData.id}/markerComment/${comment.markerCommentId}/delete`);
          // 성공적으로 삭제되면, 댓글 목록을 다시 로드
          loadComments(markerData, commentsDiv);
        } catch (error) {
          console.error('Failed to delete comment:', error);
        }
      };
      commentElement.appendChild(deleteButton);

      commentsDiv.appendChild(commentElement);
    });
  } catch (error) {
    console.error('Failed to load comments:', error);
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


async function refreshOverlay(markerData, marker) {
  if (currentOpenOverlay) {
    currentOpenOverlay.close();
  }
  const newOverlay = await createNewOverlay(markerData, marker);
  newOverlay.open(map, marker);
  currentOpenOverlay = newOverlay;
}

</script>

<style scoped>
.map-wrap {
  position: relative;

  /* Your other styles... */
}

.create-marker-btn {


}

.modal-component {
  position: absolute;
  top: 80%; /* adjust as necessary */
  left: 50%; /* adjust as necessary */
  transform: translate(-50%, -50%); /* centers the modal */
  z-index: 10; /* ensure it's above other content */
  /* rest of your styles */
}

</style>
