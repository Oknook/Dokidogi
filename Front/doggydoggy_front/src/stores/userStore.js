import { defineStore } from 'pinia';

/*
  연습용으로 설정한 Pinia UserStore인데 아마 개발하면서 싹 바꾸셔야 할겁니다.

*/
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
