import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useUserStore = defineStore('user', {
  state: () => ({
    user: {
      nickname: '',
      age: '',
      sex: '',
    },
    token: ref(null), // Using ref here isn't typical for Pinia's state since Pinia is reactive by default. This line should be token: '',
  }),
  getters: {
    isLogin: (state) => state.token !== null && state.token !== '', // Simplified the logic here. No need for computed since getters are already reactive.
  },
  actions: {
    setUser(nickname) {
      this.user.nickname = nickname;
    },
    setToken(token) {
      this.token = token;
    },
  },
});