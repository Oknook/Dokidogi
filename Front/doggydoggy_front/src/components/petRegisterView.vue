<template>
  <div class="container">
    <div class="head_line">
      <div class="move_back" @click="moveBack"><h1>←</h1></div>
      <h1 class="pet_register_title">펫 등록</h1>
    </div>
    <div class="content">
      <div class="side_menu" :class="{ 'hidden-menu': !isSideMenuOpen }">
        <ul class="side_list">
          <div class="user_menu">
            <li class="side_title">회원정보</li>
            <ol @click="userEdit" class="active_btn">
              정보수정
            </ol>
          </div>
          <div class="pet_menu">
            <li class="side_title">마이펫</li>
            <ol class="pet_register">
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
      <form @submit.prevent="petRegister" class="pet_register_form">
        <div class="pet_info">
          <div class="pet_top">
            <div class="pet_image_box">
              <img
                class="pet_image"
                v-if="petimage"
                :src="petimage"
                alt="pet_image"
              />
              <label for="pet_image_input">
                <div class="move_label" v-if="!petimage">
                  <span class="add_icon">+</span>
                  <span>이미지를 올려주세요</span>
                </div>
              </label>
              <label for="pet_image_input"
                ><div v-if="petimage" class="change_image">
                  <span>이미지 변경</span>
                </div></label
              >
              <input
                id="pet_image_input"
                type="file"
                accept="image/*"
                @change="imageUpload"
              />
            </div>
            <div class="pet_info_box">
              <div class="pet_info_detail">
                <span>이름</span>
                <input
                  class="info_text"
                  autocomplete="off"
                  type="text"
                  v-model="petname"
                />
              </div>
              <div class="pet_info_detail">
                <span>종</span>
                <input
                  class="info_text"
                  autocomplete="off"
                  type="text"
                  v-model="breeds"
                />
              </div>
              <div class="gender_info_detail">
                <span>성별</span>
                <span class="gender_icon" id="gender_male">♂</span>
                <input
                  class="checkbox"
                  type="checkbox"
                  v-model="gender"
                  @change="checkGender"
                  value="♂"
                />
                <span class="gender_icon" id="gender_female">♀</span>
                <input
                  class="checkbox"
                  type="checkbox"
                  v-model="gender"
                  @change="checkGender"
                  value="♀"
                />
              </div>
              <div class="pet_info_detail">
                <span>나이</span>
                <input
                  class="info_text"
                  autocomplete="off"
                  type="number"
                  v-model="age"
                  min="1"
                />
              </div>
            </div>
          </div>
          <div class="pet_info_introduce">
            <span>소개</span><br />
            <textarea
              class="info_text"
              id="pet_introduce"
              type="text"
              autocomplete="off"
              v-model="introduce"
            ></textarea>
          </div>
          <button type="submit" class="pet_register_btn">펫 등록하기</button>
        </div>
      </form>
    </div>
  </div>
</template>
<script>
import axios from 'axios'
export default {
  name: 'Register',
  data() {
    return {
      petimage: '',
      petname: '',
      breeds: '',
      gender: [],
      age: '',
      introduce: '',
      username: '',
      isSideMenuOpen: true,
      sideMenuWidth: 0, // side_menu의 초기 width 값
      screenWidth:
        window.innerWidth ||
        document.documentElement.clientWidth ||
        document.body.clientWidth
    }
  },
  watch: {
    gender: function () {
      console.log('Selected gender: ' + this.gender)
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
    checkGender() {
      // gender 배열에 요소가 두 개 이상인 경우 두 번째 요소를 제거
      if (this.gender.length > 1) {
        this.gender.pop()
      }
    },
    moveBack() {
      this.$router.push({ path: '/user/profile' })
    },
    async imageUpload(e) {
      const image = e.target.files[0]
      if (image) {
        const resizedImageFile = await this.resizeImage(image, 300, 300)
        const reader = new FileReader()
        reader.onload = () => {
          this.petimage = reader.result
        }
        reader.readAsDataURL(resizedImageFile)
      }
    },
    async resizeImage(imageFile, width, height) {
      return new Promise((resolve) => {
        const img = new Image()
        img.src = URL.createObjectURL(imageFile)
        img.onload = () => {
          const canvas = document.createElement('canvas')
          canvas.width = width
          canvas.height = height
          const ctx = canvas.getContext('2d')
          ctx.drawImage(img, 0, 0, width, height)

          canvas.toBlob((blob) => {
            resolve(new File([blob], imageFile.name, { type: imageFile.type }))
          }, imageFile.type)
        }
      })
    },
    async petRegister() {
      const petInfo = {
        username: this.username,
        petimage: this.petimage,
        petname: this.petname,
        breeds: this.breeds,
        gender: this.gender,
        age: this.age,
        introduce: this.introduce
      }
      //   const nameReg = /^[가-힣]{1,8}/ // 한글 1~8자까지
      //   const breedsReg = /^[가-힣]{1,15}/ //한글 최대 15자
      //   const ageReg = /^[0,9]{1,2}/ // 숫자만 최대 2자

      //   const [result1, result2, result3] = [
      //     nameReg.test(this.username),
      //     breedsReg.test(this.password),
      //     ageReg.test(this.email)
      //   ]
      await axios
        .post('http://localhost:3000/mypage/register', petInfo)
        .then((res) => {
          console.log(res)
          this.$router.push('/mypage')
        })
        .catch((err) => {
          console.log('펫 등록중 오류가 발생했습니다.:', err)
        })
    }
  },
  beforeUnmount() {
    window.removeEventListener('resize', this.handleResize)
  },
  mounted() {
    this.username = document.cookie.split('=')[1]
    console.log(this.username)

    window.addEventListener('resize', this.handleResize)
    this.handleResize()
  }
}
</script>
<style scoped>
input[type='number']::-webkit-inner-spin-button,
input[type='number']::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}
.head_line {
  display: flex;
}
.pet_register_title {
  margin: auto;
}
.move_back {
  background: #556b2f;
  border-radius: 10px;
  margin: 10px;
  padding: 0 10px;
  padding-bottom: 5px;
  color: #f9ebde;
  transition: 0.1s;
}
.move_back:hover {
  transform: scale(1.05);
  background: rgb(198, 111, 17);
  cursor: pointer;
}
.content {
  display: flex;
  width: 100%;
  height: 100%;
  margin: 0;
}
.side_menu {
  margin: 0;
  width: 30%;
  max-width: 300px;
  min-width: 250px;
  padding: 10px 30px;
  margin-right: 10px;
  background: #556b2f;
  overflow: hidden;
  transition: 0.2s linear;
}
@media screen and (max-width: 600px) {
  .side_menu {
    display: none;
  }
}
.user_menu {
  margin-bottom: 30px;
}
.active_btn {
  transition: 0.3s;
  margin-left: -20px;
  white-space: nowrap;
}
.active_btn:hover {
  border-bottom: 1px solid #f9ebde;
  transform: translateX(5px);
  color: #f9ebde;
  background: rgb(198, 111, 17);
  box-shadow: 1px 1px 5px gray;
  cursor: pointer;
}
.pet_register {
  background: rgb(198, 111, 17);
  border-bottom: 1px solid #f9ebde;
}
.side_list {
  width: 400px;
  min-width: 200px;
  color: #f9ebde;
  width: 100%;
  list-style: none;
  transition: 0.1s;
  margin-bottom: 5px;
  transition: 0.3s;
}
.side_title {
  font-weight: 800;
  font-size: 1.2em;
  display: inline-block;
  margin-bottom: 10px;
}
.pet_info {
  width: 100%;
  height: 100%;
  border: 1px solid gray;
}
.pet_top {
  display: flex;
  border-radius: 1px solid gray;
}
@media screen and (max-width: 600px) {
  .pet_top {
    display: block; /* 미디어 쿼리가 적용될 때, display를 block으로 변경 */
  }
}
.pet_info_box {
  margin: 10px;
  position: relative;
}
.info_text {
  border: none;
  width: 70%;
  height: 30px;
  padding: 10px;
  border-radius: 30px;
  border: bold #556b2f;
  background: #f9ebde;
  font-size: 1.5rem;
  opacity: 0.6;
  color: #f9ebde;
}
.info_text:focus {
  opacity: 1;
  box-shadow: 3px 3px 5px gray;
}
.pet_image_box {
  width: 300px;
  height: 300px;
  border: 5px solid #815854;
  margin: 10px;
  box-shadow: 5px 5px 10px gray;
  display: inline-block;
}
.pet_register_btn {
  background: #815854;
  color: #f9ebde;
  border: none;
  border-radius: 10px;
  width: 100px;
  height: 50px;
  transition: 0.15s;
  box-shadow: 3px 3px 7px gray;
  margin: 10px;
  float: right;
}
.pet_register_btn:hover {
  transform: scale(1.05);
  color: #f9ebde;
  background: rgb(198, 111, 17);
  box-shadow: 5px 5px 10px gray;
  cursor: pointer;
}
.pet_register_btn:active {
  background: rgb(198, 111, 17);
  box-shadow: -3px -3px 5px gray;
}
.gender_icon {
  font-size: 1.8rem;
  font-weight: 800;
  margin-right: 10px;
}
#gender_male {
  margin-left: 80px;
  color: blue;
}
#gender_female {
  color: red;
}
input[type='checkbox'] {
  width: 25px;
  margin-right: 10px;
}
#pet_image_input {
  display: none;
}
.move_label {
  margin: 80px auto;
  width: 70%;
  height: 35%;
  text-align: center;
  background: #556b2f;
  color: #f9ebde;
  cursor: pointer;
  border-radius: 20px;
  display: flex;
  flex-direction: column;
  animation: addImage 1s alternate infinite linear;
}
@keyframes addImage {
  from {
    transform: scale(1) rotate(-3deg);
  }
  to {
    transform: scale(1.1) rotate(3deg);
  }
}
.change_image {
  width: 40%;
  height: 15%;
  text-align: center;
  background: #815854;
  border-radius: 15px;
  color: #f9ebde;
  position: relative;
  margin: 5px auto;
  transition: 0.15s;
  display: flex;
  justify-content: center;
  align-items: center;
}
.change_image:hover {
  transform: scale(1.05);
  color: #f9ebde;
  background: rgb(198, 111, 17);
  box-shadow: 5px 5px 10px gray;
  cursor: pointer;
}
.change_image:active {
  background: rgb(198, 111, 17);
  box-shadow: -3px -3px 5px gray;
}
.add_icon {
  font-size: 3rem;
}
.pet_info_detail {
  margin-bottom: 30px;
  position: relative;
  display: flex;
  justify-content: flex-end;
}
.gender_info_detail {
  margin-bottom: 30px;
  position: relative;
  display: flex;
}
.gender_info_detail > span:first-child {
  position: absolute;
  left: 0;
  top: 5px;
  background: #815854;
  color: #f9ebde;
  box-shadow: 3px 3px 8px gray;
  border-radius: 5px;
  padding: 5px;
}
.pet_info_introduce {
  margin: 30px 10px 0 10px;
}
.pet_info_introduce > span {
  background: #815854;
  color: #f9ebde;
  box-shadow: 3px 3px 8px gray;
  border-radius: 5px;
  padding: 5px;
  display: inline-block;
}
#pet_introduce {
  width: 94%;
  min-height: 200px;
  padding: 10px;
  margin: 10px auto;
  font-size: 1.2rem;
}
.pet_info_detail > span:first-child {
  position: absolute;
  left: 0;
  top: 5px;
  background: #815854;
  color: #f9ebde;
  box-shadow: 3px 3px 8px gray;
  border-radius: 5px;
  padding: 5px;
}
.hidden-menu {
  display: none;
}
.toggle-button-container {
  position: relative;
}

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
