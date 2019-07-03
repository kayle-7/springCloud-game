package com.springCloud.game.config.client.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class ConfigController {

    @Value("${gameConfigName}")
    private String gameName;

    @RequestMapping("/getGameName")
    public String getGameName() {
        return gameName;
    }
}
