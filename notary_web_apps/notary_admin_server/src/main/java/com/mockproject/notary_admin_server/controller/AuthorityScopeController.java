package com.mockproject.notary_admin_server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.mockproject.notary_admin_server.dto.ApiSuccessResponse;
import com.mockproject.notary_admin_server.service.AuthorityScopeService;
import com.mockproject.notary_common.constant.AuthorityType;
import com.mockproject.notary_common.entity.notary.AuthorityScope;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/commissions")
public class AuthorityScopeController {

    private final AuthorityScopeService authorityScopeService;

    public AuthorityScopeController(AuthorityScopeService authorityScopeService) {
        this.authorityScopeService = authorityScopeService;
    }

    @PostMapping("/{id}/authority-scopes")
    public ApiSuccessResponse<AuthorityScope> create(
            @PathVariable("id") UUID commissionId,
            @RequestBody AuthorityScope authorityScope) {

        return ApiSuccessResponse.created(
                authorityScopeService.create(commissionId, authorityScope.getAuthorityType()));
    }

    @GetMapping("/{id}/authority-scopes")
    public ApiSuccessResponse<List<AuthorityScope>> getByCommission(
            @PathVariable("id") UUID commissionId) {

        return ApiSuccessResponse.ok(
                authorityScopeService.getByCommission(commissionId));
    }

    @DeleteMapping("{id}/authority-scopes/{authorityScopesId}")
    public ApiSuccessResponse<Void> delete(
            @PathVariable("id") UUID commissionId,
            @PathVariable("authorityScopesId") UUID authorityScopesId) {

        authorityScopeService.delete(authorityScopesId);
        return ApiSuccessResponse.deleted();
    }
}