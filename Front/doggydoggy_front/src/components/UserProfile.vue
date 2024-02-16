<template>
  <div class="container">
    <div class="content">
      <div class="side_menu" :class="{ 'hidden-menu': !isSideMenuOpen }">
        <h2 class="mypage_title">프로필 페이지</h2>
        <ul class="side_list">
          <div class="user_menu">
            <li class="side_title">회원정보</li>
            <ol @click="userEdit">
              수정하기
            </ol>
          </div>
          <div class="pet_menu">
            <li class="side_title">마이펫</li>
            <ol @click="registerPet" class="pet_register">
              펫 등록하기
            </ol>
          </div>
        </ul>
      </div>
      <div class="toggle-button-container">
        <button @click="toggleSideMenu" class="toggle-button">
          {{ isSideMenuOpen ? '<<' : '>>' }}
        </button>
      </div>
      
      <div class="petlist">
         <p>Hi</p>
        <p></p> 
      </div>
    </div>
  </div>
</template>
<script>
import axios from 'axios'
export default {
  name: 'Mypage',
  data() {
    return {
      exist: '펫 등록을 해주세요.',
      username: '',
      petInfo: {},
      isSideMenuOpen: true,
      sideMenuWidth: 0, // side_menu의 초기 width 값
      screenWidth:
        window.innerWidth ||
        document.documentElement.clientWidth ||
        document.body.clientWidth
    }
  },
  created() {
    document.title = this.$route.name
    window.addEventListener('resize', this.handleResize)
    this.handleResize() // 페이지 로드 시 초기 상태 설정
  },
  methods: {
    toggleSideMenu() {
      // 스크린 너비가 600px 이하인 경우에만 토글로 열림/닫힘 상태 변경
      this.isSideMenuOpen = !this.isSideMenuOpen
    },
    handleResize() {
      // 스크린 너비 업데이트
      this.screenWidth =
        window.innerWidth ||
        document.documentElement.clientWidth ||
        document.body.clientWidth

      // 스크린 너비가 600px 이하인 경우, 메뉴를 닫도록 설정
      if (this.screenWidth <= 600) {
        this.isSideMenuOpen = false
      } else {
        // 스크린 너비가 600px 초과인 경우, 메뉴를 열도록 설정
        this.isSideMenuOpen = true
      }
    },  
    userEdit() {
      this.$router.push({ path: '/user/change' })
    },
    registerPet() {
      this.$router.push({ name: 'register' })
    },       
  },
  beforeUnmount() {
    window.removeEventListener('resize', this.handleResize)
  },
  async mounted() {
    this.username = document.cookie.split('=')[1]
    await axios
      .get('http://localhost:5173/user/profile', {
        params: { username: this.username }
      })
      .then((res) => {
        this.petInfo = res.data
        console.log(this.petInfo)
      })
      .catch((e) => {
        console.log('mypage 데이터를 불러오는 중 에러가 발생했습니다.:', e)
      })

    window.addEventListener('resize', this.handleResize)
    this.handleResize()

    const petList = this.$refs.petList

    petList.addEventListener('wheel', (event) => {
      if (event.deltaY !== 0) {
        event.preventDefault()
        petList.scrollLeft += event.deltaY
      }
    })
  }
}
</script>
<style scoped>
.container {
  width: 100%;
  height: 100%;
  min-height: 80vh;
}
.mypage_title {
  text-align: center;
  color: white;
  text-shadow: 1px 1px 2px #556b2f;
  margin: 1rem 0 2rem 0;
}
.mypage_info_exist {
  text-align: center;
}
.content {
  display: flex;
  margin: 3rem 0;
  width: 100%;
  height: 600px;
}
.side_menu {
  margin: 0;
  width: 30%;
  border-radius: 10px;
  max-width: 300px;
  min-width: 250px;
  padding: 10px 40px;
  margin-right: 10px;
  background: #556b2f;
  overflow: hidden;
  transition: 0.2s linear;
  align-items: center;
}
@media screen and (max-width: 600px) {
  .side_menu {
    display: none;
  }
}
.user_menu {
  margin-bottom: 30px;
}
.side_list {
  width: 100%;
  color: #f9ebde;
  list-style: none;
  margin-bottom: 5px;
  transition: 0.3s;
}
.side_title {
  font-weight: 800;
  font-size: 1.2em;
  display: inline-block;
  margin-bottom: 10px;
}
.pet_list {
  display: flex;
  width: 100%;
  overflow-x: auto;
}

/** 사이드 메뉴 세부 리스트(회원정보/마이펫 영역) */
ol {
  transition: 0.3s;
  margin-left: -20px;
  max-width: 120px;
}
ol:hover {
  border-bottom: 1px solid #f9ebde;
  transform: translateX(5px);
  color: #f9ebde;
  border-radius: 10px;
  background: rgb(219, 58, 58);
  box-shadow: 0 0 1px rgb(247, 247, 247);
  cursor: pointer;
}
ol:active {
  box-shadow: -1px -1px 5px gray;
}
.pet_card {
  width: 350px;
  border: 3px solid #ed7065;
  padding-bottom: 40px;
  margin: 10px;
  border-radius: 10px;
  background: rgb(255, 255, 255);
  position: relative;
  box-shadow: 5px 5px 10px gray;
  transition: 0.15s;
}
.pet_card:hover {
  transform: translateY(-10px);
}
.pet_image_box {
  text-align: center;
  -webkit-box-shadow: 0px 20px 0px -10px #ffffff, 0px -20px 0px -10px #ffffff,
    20px 0px 0px -10px #ffffff, -20px 0px 0px -10px #ffffff,
    0px 0px 0px 10px #556b2f, 5px 5px 15px 5px rgba(0, 0, 0, 0);
  box-shadow: 0px 20px 0px -10px #ffffff, 0px -20px 0px -10px #ffffff,
    20px 0px 0px -10px #ffffff, -20px 0px 0px -10px #ffffff,
    0px 0px 0px 10px #ff0000, 5px 5px 15px 5px rgba(0, 0, 0, 0);
  margin: 1rem;
}

/** 펫 카드 디테일 정보 */
.pet_info {
  margin: 2.5rem 10px 10px 10px;
}
.pet_info_detail {
  margin-bottom: 1px;
}
.pet_info_not {
  margin: auto;
}
.pet_list::-webkit-scrollbar {
  height: 10px;
}

.pet_list::-webkit-scrollbar-track {
  background-color: green;
  border-radius: 20px;
}

.pet_list::-webkit-scrollbar-thumb {
  background-color: #dd3737;
  border-radius: 20px;
}

.pet_list::-webkit-scrollbar-thumb:hover {
  background-color: #f5963c;
}
.pet_info_detail > span:first-child {
  background: #e15c50;
  display: inline-block;
  color: #f9ebde;
  border-radius: 5px;
  margin-right: 10px;
  margin-bottom: 10px;
  padding: 3px;
}

/** 카드 내부 버튼(수정/삭제) */
.card_btn_box {
  position: absolute;
  bottom: 5px;
  right: 5px;
  opacity: 0;
  transition: 0.2s ease-in;
}
.card_btn {
  margin: 0 5px;
  border: none;
  width: 60px;
  height: 30px;
  border-radius: 5px;
  transition: 0.1s ease;
}
.card_btn:hover {
  transform: scale(1.05);
  box-shadow: 3px 3px 10px gray;
}
.card_btn:active {
  box-shadow: -1px -1px 5px gray;
}
#edit_btn {
  background: rgb(97, 191, 20);
}
#delete_btn {
  background: rgb(236, 120, 52);
}
#edit_btn:hover {
  background: rgb(145, 227, 78);
  cursor: pointer;
}
#delete_btn:hover {
  background: rgb(250, 132, 63);
  cursor: pointer;
}
.pet_card:hover .card_btn_box {
  opacity: 1;
  transform: translate(0, -10px);
}
.hidden-menu {
  display: none;
}
.toggle-button-container {
  position: relative;
}

/* 사이드 바 토글 버튼 */
.toggle-button {
  position: relative;
  right: 10px; /* 사이드 메뉴 오른쪽에 배치 */
  top: 250px;
  background-color: #613d3a;
  color: #f9ebde;
  font-size: 1.2rem;
  border: none;
  border-top-right-radius: 10px;
  border-bottom-right-radius: 10px;
  width: 35px;
  height: 100px;
  cursor: pointer;
}
</style>
