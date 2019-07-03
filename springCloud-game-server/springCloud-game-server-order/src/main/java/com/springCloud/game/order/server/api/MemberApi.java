package com.springCloud.game.order.server.api;

import com.springCloud.game.member.api.memberApi.MemberService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "springCloud-game-server-member"/*, fallback = MemberApiFallback.class*/)
public interface MemberApi extends MemberService {
}
