package com.mockproject.notary_admin_server.controller;

import com.mockproject.notary_admin_server.service.impl.NotaryService;
import com.mockproject.notary_common.entity.notary.Notary;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/notaries")
@RequiredArgsConstructor
public class NotaryController {

    private final NotaryService notaryService;

    @GetMapping()
    public Set<Notary> getAllNotaries() {
        return notaryService.getAllNotaries();
    }
}
