package com.mockproject.notary_admin_server.service;

import java.util.List;
import java.util.UUID;

import com.mockproject.notary_common.constant.AuthorityType;
import com.mockproject.notary_common.entity.notary.AuthorityScope;

public interface AuthorityScopeService {
    AuthorityScope create(UUID commissionId, AuthorityType authorityType);

    List<AuthorityScope> getByCommission(UUID commissionId);

    void delete(UUID id);
}
