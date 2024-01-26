const CLIENT_ID = 'e04f45942acd0ef42b82667406b06646';
const REDIRECT_URI = 'http://localhost:5173/login/waiting';

export const KAKAO_AUTH_URL = `https://kauth.kakao.com/oauth/authorize?client_id=${CLIENT_ID}&redirect_uri=${REDIRECT_URI}&response_type=code`;
