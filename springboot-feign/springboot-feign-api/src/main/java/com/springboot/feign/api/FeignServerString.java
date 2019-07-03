package com.springboot.feign.api;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

public interface FeignServerString {

    @RequestMapping("/getFeignServerUser")
    User getFeignServerUser();

    @RequestMapping("/getFeignServerUserList")
    List<User> getFeignServerUserList();

}
