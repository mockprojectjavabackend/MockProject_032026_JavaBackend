package com.mockproject.notary_admin_server.service;

import com.mockproject.notary_admin_server.dto.request.AdminInviteNotaryRequest;
import com.mockproject.notary_admin_server.dto.response.NotaryInviteResponse;

public interface AdminNotaryService {
    NotaryInviteResponse inviteNotary(AdminInviteNotaryRequest request);
}
