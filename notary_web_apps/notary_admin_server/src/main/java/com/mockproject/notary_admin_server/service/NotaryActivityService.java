package com.mockproject.notary_admin_server.service;

import com.mockproject.notary_admin_server.dto.response.RecentActivityResponse;
import java.util.List;
import java.util.UUID;

public interface NotaryActivityService {
    List<RecentActivityResponse> getRecentActivities(UUID notaryId, int limit);
}