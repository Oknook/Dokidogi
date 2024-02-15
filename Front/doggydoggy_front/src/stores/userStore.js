import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => ({
    user: {
      nickname: '',
      age: '',
      sex: '',
    },
    token: '', // Adjusted from ref(null) to '', as Pinia state is reactive by default
    isLoggedIn: false, // Added to manage login status
  }),
  getters: {
    isLogin: (state) => state.isLoggedIn, // Updated to reflect the new login status state
  },
  actions: {
    setUser(nickname) {
      this.user.nickname = nickname;
    },
    setToken(token) {
      this.token = token;
    },
    // New method to set login status
    setLoginStatus(status) {
      this.isLoggedIn = status;
      console.log('로그인', status)
    },
  },
});