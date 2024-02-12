package com.ssafy.dokidog2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    //    public static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";
    private String resourcePath = "/images/**"; // view 에서 접근할 경로
//    private String savePath = "file:///C:/Users/SSAFY/imgtest/"; // 실제 파일 저장 경로(win) 싸트북
        private String savePath = "file:///C:/Users/zxcas/imgtest/"; // 실제 파일 저장 경로(win) 싸트북
    // 위 코드는 /images/** 경로로 들어오는 요청을 C:/Users/SSAFY/imgtest/ 위치에 있는 파일로 매핑합니다.
// 예를 들어, 클라이언트가 /images/sample.jpg URL로 요청하면, C:/Users/SSAFY/imgtest/sample.jpg 파일이 반환됩니다.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourcePath)
            .addResourceLocations(savePath);
    }


}
