package com.ssafy.dokidog2.util;

import com.ssafy.dokidog2.user.dto.CustomOAuth2User;
import com.ssafy.dokidog2.user.dto.CustomOAuthUser;
import com.ssafy.dokidog2.user.dto.UserDTO;
import com.ssafy.dokidog2.user.entity.User;
import com.ssafy.dokidog2.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JWTFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    public JWTFilter(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
        //cookie들을 불러온 뒤 Authorization Key에 담긴 쿠키를 찾음
        String authorization = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            System.out.println(cookie.getName());
            if (cookie.getName().equals("Authorization")) {

                authorization = cookie.getValue();
            }
        }

        //Authorization 헤더 검증
        if (authorization == null) {

            System.out.println("token null");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰
        String token = authorization;

        //토큰 소멸 시간 검증
        if (jwtTokenProvider.isExpired(token)) {

            System.out.println("token expired");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰에서 username과 role 획득
        User user = userRepository.findByUserId(jwtTokenProvider.getUserId(token));

        //userDTO를 생성하여 값 set
        CustomOAuthUser customOAuthUser = new CustomOAuthUser();
        customOAuthUser.setCompanyName(user.getCompanyName());
        customOAuthUser.setCompanyId(user.getCompanyId());
        customOAuthUser.setRole(user.getGrade());

        //UserDetails에 회원 정보 객체 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(customOAuthUser);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}