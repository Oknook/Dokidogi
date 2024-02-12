package com.ssafy.dokidog2.user.service;

import com.ssafy.dokidog2.user.dto.GoogleResponse;
import com.ssafy.dokidog2.user.dto.KakaoResponse;
import com.ssafy.dokidog2.user.dto.NaverResponse;
import com.ssafy.dokidog2.user.dto.OAuth2Response;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomOAuth2UserService {}
//    extends DefaultOAuth2UserService {
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//
//        log.info(oAuth2User.toString());
//
//        String registrationId = userRequest.getClientRegistration().getRegistrationId();
//        OAuth2Response oAuth2Response = null;
//        if (registrationId.equals("naver")) {
//            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
//        }
//        else if (registrationId.equals("google")) {
//            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
//        }
//        else if (registrationId.equals("kakao")) {
//            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
//        }
//        else {
//            return null;
//        }
//		return oAuth2User;
//    }
//}