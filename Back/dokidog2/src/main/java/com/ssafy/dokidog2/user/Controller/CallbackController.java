package com.ssafy.dokidog2.user.Controller;

import com.ssafy.dokidog2.user.service.GoogleService;
import com.ssafy.dokidog2.user.service.KakaoService;
import com.ssafy.dokidog2.user.service.NaverService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/callback")
public class CallbackController {

    private final GoogleService googleService;
    private final KakaoService kakaoService;
    private final NaverService naverService;

    //    @GetMapping("/kakao")
//    public String kallback(HttpServletRequest request) throws Exception {
//        String code = request.getParameter("code");
//        String  kakaoInfo = kakaoService.getKakaoInfo(code);
//        log.info("kakao dto : "+ kakaoInfo);
//        return kakaoInfo;
//    }
    @GetMapping("/kakao")
    public RedirectView kakaoCallback(HttpServletRequest request) throws Exception {
        String code = request.getParameter("code");
        String kakaoInfo = kakaoService.getKakaoInfo(code);
        log.info("kakao dto : " + kakaoInfo);
        // 여기서 RedirectView를 사용하여 다른 URL로 리다이렉트
        return new RedirectView("https://i10b202.p.ssafy.io/user/input?token=" + kakaoInfo);
    }

    @GetMapping("/naver")
    public String nallback(HttpServletRequest request) throws Exception {
        String naverInfo = naverService.getNaverInfo(request.getParameter("code"));
        log.info("naver dto ; " + naverInfo);
        return naverInfo;
    }

    @GetMapping("/google")
    public String gallback(HttpServletRequest request) throws Exception {
        String googleInfo = googleService.getGoogleInfo(request.getParameter("code"));
        log.info("google dto : " + googleInfo);
        return googleInfo;
    }
}
