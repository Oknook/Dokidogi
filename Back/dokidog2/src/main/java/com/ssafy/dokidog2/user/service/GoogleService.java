package com.ssafy.dokidog2.user.service;

import com.ssafy.dokidog2.user.dto.SocialDTO;
import com.ssafy.dokidog2.user.entity.User;
import com.ssafy.dokidog2.user.repository.UserRepository;
import com.ssafy.dokidog2.util.JwtTokenProvider;

import com.ssafy.dokidog2.util.UserGrade;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class GoogleService {

	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;
	public GoogleService(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.userRepository = userRepository;
	}

	// https://antdev.tistory.com/72
//	private final String GOOGLE_E_MAIL = "https://www.googleapis.com/auth/userinfo.profile";
	private final String GOOGLE_SNS_CLIENT_ID = "25526902806-o0qoq0iel49ca0sev5vrptq86i6kdmed.apps.googleusercontent.com";
	private final String GOOGLE_SNS_CALLBACK_URL = "http://i10b202.p.ssafy.io/api/callback/google";
	private final String GOOGLE_SNS_CLIENT_SECRET = "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";
	private final String GOOGLE_SNS_TOKEN_BASE_URL = "https://oauth2.googleapis.com/token";

	public String getGoogleInfo(String code) throws Exception {
		String accessToken = "";
		String refreshToken = "";
		String idToken = "";
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-type", "application/x-www-form-urlencoded");

			MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			params.add("code", code);
			params.add("client_id", GOOGLE_SNS_CLIENT_ID);
			params.add("client_secret", GOOGLE_SNS_CLIENT_SECRET);
			params.add("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
			params.add("grant_type", "authorization_code");

//			String parameterString = params.entrySet().stream().map(x -> x.getKey() + "=" + x.getValue())
//					.collect(Collectors.joining("&"));

			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

			ResponseEntity<String> response = restTemplate.exchange(
					GOOGLE_SNS_TOKEN_BASE_URL,
					HttpMethod.POST,
					httpEntity,
					String.class);

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
			log.info("google json : ", jsonObj);

			accessToken = (String) jsonObj.get("access_token");
			refreshToken = (String) jsonObj.get("refresh_token");
			idToken = (String) jsonObj.get("id_token");
		} catch (Exception e) {
			throw new IllegalArgumentException("알 수 없는 구글 로그인 Access Token 요청 URL 입니다 :: " + GOOGLE_SNS_TOKEN_BASE_URL);
		}
		return getUserInfoWithToken(idToken);
	}
	
	private String getUserInfoWithToken(String idToken) throws Exception {

        //HttpHeader 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded");

        //HttpHeader 담기
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
        		"https://oauth2.googleapis.com/tokeninfo?id_token="+idToken,
        		HttpMethod.POST,
				httpEntity,
				String.class);

        //Response 데이터 파싱
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj    = (JSONObject) jsonParser.parse(response.getBody());

		log.info(jsonObj.toJSONString());
		String id = (String) jsonObj.get("sub");

		Long checkId = userRepository.findByCompanyIdAndCompanyName(id, "google");
		long time = System.currentTimeMillis();
		if (checkId == null) {
			User user = new User();
			user.setCompanyId(id);
			user.setCompanyName("google");
			user.setGrade(UserGrade.TEMPORARY);
			user.setRegDttm(LocalDateTime.now());
			User savedUser = userRepository.save(user);
			
			long userId = savedUser.getUserId();
			UserGrade grade = savedUser.getGrade();
			return jwtTokenProvider.createAccessToken(userId, grade, time);
		}
		else {
			User user = userRepository.findByUserId(checkId);
			long userId = user.getUserId();
			UserGrade grade = user.getGrade();
			return jwtTokenProvider.createAccessToken(userId, grade, time);
		}
    }
}
