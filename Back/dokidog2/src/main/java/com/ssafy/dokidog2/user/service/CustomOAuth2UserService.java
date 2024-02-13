package com.ssafy.dokidog2.user.service;

import com.ssafy.dokidog2.user.dto.CustomOAuth2User;
import com.ssafy.dokidog2.user.dto.CustomOAuthUser;
import com.ssafy.dokidog2.user.dto.GoogleResponse;
import com.ssafy.dokidog2.user.dto.KakaoResponse;
import com.ssafy.dokidog2.user.dto.NaverResponse;
import com.ssafy.dokidog2.user.dto.OAuth2Response;
import com.ssafy.dokidog2.user.entity.User;
import com.ssafy.dokidog2.user.repository.UserRepository;
import com.ssafy.dokidog2.util.UserGrade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info(oAuth2User.toString());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }
        else {
            return null;
        }
        String companyName = oAuth2Response.getProvider();
        String companyId = oAuth2Response.getProviderId();
        Long userId = userRepository.findByCompanyIdAndCompanyName(companyId, companyName);
        User exisUser = userRepository.findByUserId(userId);

        if (exisUser == null) {

            User user = new User();
            user.setCompanyName(companyName);
            user.setCompanyId(companyId);
            user.setGrade(UserGrade.TEMPORARY);

            userRepository.save(user);

            CustomOAuthUser customOAuthUser = new CustomOAuthUser();
            customOAuthUser.setCompanyName(companyName);
            customOAuthUser.setCompanyId(companyId);
            customOAuthUser.setRole(UserGrade.TEMPORARY);

            return new CustomOAuth2User(customOAuthUser);
        }
        else {
            CustomOAuthUser customOAuthUser = new CustomOAuthUser();
            customOAuthUser.setCompanyName(companyName);
            customOAuthUser.setCompanyId(companyId);
            customOAuthUser.setRole(exisUser.getGrade());
            return new CustomOAuth2User(customOAuthUser);
        }
    }
}