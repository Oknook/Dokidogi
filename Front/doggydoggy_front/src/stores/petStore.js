import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { useRouter } from 'vue-router'
import axios from 'axios'


export const usePetStore = defineStore('pet', {
    state: () => ({
        petData: null,
        username: '',
    }),
    actions: {
        setPetData(data) {
            this.petData = data;
        },
        setUsername(name) {
            this.username = name;
        }
    }
})