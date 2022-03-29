package ru.hogwarts.controller;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public class InfoController {
    private final ServerProperties serverProperties;

    public InfoController(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    @GetMapping("/getPort")
    public ResponseEntity<Integer> getPortNumber() {
        int port = serverProperties.getPort();
        return ResponseEntity.ok(port);
    }
}