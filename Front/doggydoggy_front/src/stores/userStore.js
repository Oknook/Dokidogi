import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => ({
    user: {
      nickname: '',
      age: '',
      sex: '',
    },
    token: '',
  }),
  actions: {
    setUser(nickname) {
      this.user.nickname = nickname;
    },
    setToken(token) {
      this.token = token;
    },
  },
});
