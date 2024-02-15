# Dokidogi - 모든 산책이 이야기가 되는 순간

## Dokidogi 링크 : [SSAFY 서버](https://i10b202.p.ssafy.io/)

<!-- ## 소개 영상 보기 : [UCC 링크]() -->


## 프로젝트 진행 기간
- SSAFY 10기 2학기 공통프로젝트 - Dokidogi
- 2024.01.03(수) ~ 2024.02.16(금) (45일간 진행)



## Dokidogi - 개요
반려동물에 대한 관심이 커져가는 요즘 시대에 여러분들은 자신의 반려견에게 친구를 만들어 주고 싶다는 생각을 해본 적이 있나요?

Doki Dogi는 이러한 니즈를 충족시켜주기 위해 탄생한 소셜 콘텐츠 기반 웹 서비스입니다. Doki Dogi와 함께라면, 여러분들은 손쉽게 반려견의 친구를 만들고 산책하기 좋은 장소를 알 수 있습니다.

##  Dokidogi 주요 서비스

### 지도

- 혼자보기 아쉬운 장소들을 친구들에게 자랑하고 싶을 때 제목과 설명 그리고 이미지를 업로드 해 서로에게 공유해 보세요 

- 그러면 해당 장소에 관심이 있는 유저는 업로드 된 마커 정보를 통해 자신의 취향에 맞는 장소를 감상 및 평가 할 수 있습니다.

- 나와 취향이 맞는 사람들과 댓글로 서로 대화를 주고받아요.

### 산책

- 마음에 드는 마커를 발견했는데 혼자 산책하기 아쉬울 때 마커 내 존재하는 산책 게시판을 통해 같이 산책하기를 원하는 유저를 구할 수 있어요.
- 게시글 내 존재하는 채팅방을 통해 만나기 전 서로의 생각과 느낀점을 실시간으로 나눌 수 있습니다.

- 이미 산책이 시작한 경우라도 경로 추적하기 버튼을 통해 게시판 생성자의 위치를 실시간으로 알아보고 산책을 함께 참여 할 수 있어요!

### 게시판

- 지금 하고있는 생각이나, 오늘의 계획 등을 자유롭게 작성해요

- 유저들은 댓글을 작성할 수 있고 카테고리별로 자유롭게 게시글을 조회 할 수 있어요
### 매칭

- 반려견의 친구를 만들고 싶으신데 너무 막막하다고 느낄 때. 원하는 카테고리를 설정해서 나만의 반려견 친구를 찾아 볼 수 있어요

- 그러면 내 주변 위치를 우선적으로 검색해 사용자와 거리가 가까운 유저들을 추천해줘요
## ✔ 주요 기술
**Backend - Spring**
- IntelliJ IDE
- Springboot 3.2.2
- Java zulu-17.0.9
- Spring Data JPA
- Spring Security
- Spring Validation
- Spring Web
- QueryDSL
- WebSocket
- MySQL
- MongoDB

**Frontend**
- Visual Studio Code IDE
- Vue 3
- Vite

- Vuetify 2.4.0
- Vuex 3.4.0

- Webstomp-Client 1.2.6
- Sock.js-Client 1.5.2
- sweetalert2 11.3.10

**CI/CD**
- AWS EC2
- AWS S3
- Docker
- Jenkins
- NGINX
- SSL

## ✔ 협업 툴

- Gitlab
- Notion
- JIRA
- MatterMost
- Webex
- Figma
## ✔ 협업 환경
- Gitlab
    - 코드의 버전을 관리
- JIRA
    - 매주 목표량을 설정하여 Sprint 진행, 업무의 할당량을 정하여 Story Point를 설정하고, In-Progress -> Done 순으로 작업
- 회의
    - 매일 아침 Daily Scrum 진행, 전날 목표 및 미비량 보고, 당일 할 업무 브리핑
    - 의견 충돌 발생 시 디지털 보드 및 팀원들 간의 회의를 통해 의견 조율
- Notion
    - 회의 및 피드백이 존재 할 시 회의록을 기록하여 보관 후 자체 피드백 수행
    기술확보 시, 다른 팀원들의 원할한 진행을 위해 문서화 수행
- 구글시트
    - 시퀀스다이어그램, 기능명세서,api 명세서 Gantt Chart 등 모두가 공유해야 하는 문서 관리

## ✔ 프로젝트 파일 구조
### Back
```
main
├── java
│   └── com
│       └── ssafy
│           └── dokidog2
│               ├── board
│               │   ├── controller
│               │   ├── dto
│               │   ├── entity
│               │   └── repository
│               │   └── service
│               │
│               ├── config
│               │
│               ├── map
│               │   ├── controller
│               │   ├── dto
│               │   ├── entity
│               │   ├── repository
│               │   └── service
│               │
│               ├── matching
│               │   ├── controller
│               │   ├── dto
│               │   └── service
│               │
│               ├── user
│               │   ├── Controller
│               │   ├── dto
│               │   ├── entity
│               │   ├── repository
│               │   └── service
│               │
│               └── util
│
└── resources
```
### Front
```
├─ App
├─ main
│  
├─ assets
│  ├─ style
│  │  
│  ├─ icons
│  │    
│  └─ images
│           
├─ video
│          
├─ components
│  ├─ Calendar
│  ├─ GoogleLogin
│  ├─ KakaoLogin
│  ├─ LoingHandler
│  ├─ NaverLogin
│  ├─ PetProfile
│  ├─ PetRegister
│  ├─ UserInput
│  ├─ UserProfile
│  │  
│  └─ common
│     ├─ Footer
│     └─ Header
│          
├─ router
│  └─ index
│      
├─ stores
│  ├─ comments
│  ├─ petStore
│  ├─ posts
│  └─ userStore
│      
└─ views
   ├─ BanneduserView
   ├─ BoardView
   ├─ CommentCreateView
   ├─ DestinationModal
   ├─ HomeView
   ├─ KakaoMapView
   ├─ LoginView
   ├─ LoginWaitingView
   ├─ MarkerModal
   ├─ MarkerUpdateModal
   ├─ MatchingView
   ├─ PetProfileView
   ├─ PetRegisterDetailView
   ├─ PetRegisterView
   ├─ PetRegistListView
   ├─ PostCreateView
   ├─ PostDetailUpdate
   ├─ PostDetailView
   ├─ UserInputView
   └─ UserProfileView

```
## ✔ 프로젝트 산출물
- [피그마](https://www.figma.com/file/CaSJHvTkjsIOxHP1LSTQ5i/B202?type=design&node-id=0-1&mode=design)
- [기능명세서](https://docs.google.com/spreadsheets/d/134GKlgVexmzWVXY84XzOoArdb9h-kKX7bcR94Jh9Hw0/edit#gid=1723955116)
- [API명세서](https://docs.google.com/spreadsheets/d/134GKlgVexmzWVXY84XzOoArdb9h-kKX7bcR94Jh9Hw0/edit#gid=955200680)
- [ERD](https://www.erdcloud.com/d/NfhYKc4YHmFybz72S)
- [간트차트](https://docs.google.com/spreadsheets/d/134GKlgVexmzWVXY84XzOoArdb9h-kKX7bcR94Jh9Hw0/edit#gid=300850014)

## ✔ 팀원 역할 분배
![역할 배분](https://donghotest.s3.ap-northeast-2.amazonaws.com/teammate.jpg)
