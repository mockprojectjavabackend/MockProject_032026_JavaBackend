package com.mockproject.notary_admin_server.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "UNAUTHENTICATION-CONTROLLER")
@RestController
@RequestMapping("/auth1")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UnAuthenController {
    @GetMapping("/unauthenticated")
    public String unauthenticated() {
        log.info("unauthenticated");
        return "You are not authenticated. Please log in to access this resource.";
    }


}
