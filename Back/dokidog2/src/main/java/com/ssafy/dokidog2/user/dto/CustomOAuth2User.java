package com.ssafy.dokidog2.user.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {

    private CustomOAuthUser customOAuthUser;
    public CustomOAuth2User(CustomOAuthUser customOAuthUser) {
        this.customOAuthUser = customOAuthUser;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return customOAuthUser.getRole().toString();
            }
        });
        return collection;
    }

    @Override
    public String getName() {
        return customOAuthUser.getCompanyName();
    }

    public String getId() {
        return customOAuthUser.getCompanyId();
    }
}
