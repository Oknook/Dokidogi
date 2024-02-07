<script setup>
//npm install axios swiper vue-awesome-swiper --save
import Header from '@/components/common/Header.vue'

import axios from 'axios';
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { Swiper, SwiperSlide } from 'swiper/vue';
import 'swiper/swiper-bundle.css';

const dogImages = ref([]);
const dogName = ref('');
const dogInfo = ref([]);
const ownerID = ref('');
const tags = ref([]);
const router = useRouter();

//반려견 정보를 백엔드로부터 받아오기
onMounted(async () => {
    try {
        const response = await axios.get('your-backend-url/dog-info');
        const data = response.data;
        dogImages.value = data.images; // Assuming 'images' is an array of image URLs
        dogName.value = data.name;
        dogInfo.value = data.info; // Assuming 'info' is an array of dog info strings
        ownerID.value = data.ownerID; // 견주 정보 중에서 id 받아오기
        tags.value = data.tags; // Assuming 'tags' is an array of tag strings
    } catch (error) {
        console.error(error);
    }
})

// 반려견 주인 프로필로 이동하는 라우터
function goToOwnerProfile() {
  router.push({name: 'user-profile', params: {userId: ownerID}});
}
</script>

<template>
    <div>
        <Header></Header>
        <div class="dogDetails">
            <Swiper :slidesPerView="1">
            <SwiperSlide v-for="(image, index) in dogImages" :key="index">
                <img :src="image" alt="Dog image" />
            </SwiperSlide>
            </Swiper>

            <h1>{{ dogName }}</h1>

            <div v-for="(info, index) in dogInfo" :key="index">
            <p>{{ info }}</p>
            </div>

            <h2 @click="goToOwnerProfile">{{ ownerName }}</h2>

            <div class="tags">
            <span v-for="(tag, index) in tags" :key="index">{{ tag }}</span>
            </div>
        </div>
    </div>

</template>



<style scoped>
.dogDetails {
  /* Add styles for the dog details layout */
}
.dogDetails img {
  width: 100%;
  height: auto;
  border-radius: 10px; /* Optional: if you want rounded corners */
}
.dogDetails h1 {
  /* Styles for the dog's name */
}
.dogDetails h2 {
  /* Styles for the owner's name */
  cursor: pointer; /* Indicates clickable */
}
.tags span {
  /* Styles for the tags */
}
</style>
