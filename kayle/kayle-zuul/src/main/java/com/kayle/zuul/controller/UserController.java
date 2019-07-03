package com.kayle.zuul.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author : zx
 * create at:  2019-03-25  00:04
 * @description:
 */
@RestController
public class UserController {

//    @Autowired
//    @Qualifier("redisTokenStore")
//    private TokenStore tokenStore;

    @GetMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

    @RequestMapping("/oauth/remove_token")
    public ResponseEntity<ApiResult> removeToken(@RequestParam("token") String token) {

        if (token != null) {
//            OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
//            tokenStore.removeAccessToken(accessToken);
        } else {
            return new ResponseEntity<>(ApiResult.fail("fail"), HttpStatus.OK);
        }
        return new ResponseEntity<>(ApiResult.success(), HttpStatus.OK);
    }

}

