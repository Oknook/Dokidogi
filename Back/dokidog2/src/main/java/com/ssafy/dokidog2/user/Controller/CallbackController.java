package com.ssafy.dokidog2.user.Controller;

import com.ssafy.dokidog2.user.dto.KakaoDTO;
import com.ssafy.dokidog2.user.dto.SocialDTO;
import com.ssafy.dokidog2.user.service.GoogleService;
import com.ssafy.dokidog2.user.service.KakaoService;
import com.ssafy.dokidog2.user.service.NaverService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/callback")
public class CallbackController {

    private final GoogleService googleService;
    private final KakaoService kakaoService;
    private final NaverService naverService;

    @GetMapping("/kakao")
    public String kallback(HttpServletRequest request) throws Exception {
        System.out.println("1");
        String code = request.getParameter("code");
        System.out.println(code);
        String  kakaoInfo = kakaoService.getKakaoInfo(code);
        log.info("kakao dto : "+ kakaoInfo);
        return kakaoInfo;
    }

    @GetMapping("/naver")
    public String nallback(HttpServletRequest request) throws Exception {
        String naverInfo = naverService.getNaverInfo(request.getParameter("code"));
        log.info("naver code : "+ request.getParameter("code"));
        log.info("naver dto ; "+ naverInfo);
        return naverInfo;
    }

    @GetMapping("/google")
    public String gallback(HttpServletRequest request) throws Exception {
        String googleInfo = googleService.getGoogleInfo(request.getParameter("code"));
        log.info("google code : "+ request.getParameter("code"));
        log.info("google dto : "+ googleInfo);
        return googleInfo;
    }
}
