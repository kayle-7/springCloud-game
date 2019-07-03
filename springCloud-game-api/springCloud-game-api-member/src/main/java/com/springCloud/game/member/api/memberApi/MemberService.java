package com.springCloud.game.member.api.memberApi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface MemberService {

    @RequestMapping("/getMember")
    String getMember(@RequestParam("name") String name);

    @RequestMapping("/getMemberTimeout")
    String getMemberTimeout(@RequestParam("name") String name);

    @RequestMapping("/getMemberHystrix")
    String getMemberHystrix(@RequestParam("name") String name);
}
