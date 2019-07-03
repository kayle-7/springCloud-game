package com.kayle.web.controller.app.contract;

import com.kayle.contract.app.AppService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "kayle-server")
public interface AppApi extends AppService {
}
