package com.mockproject.notary_admin_server.service;

import com.mockproject.notary_admin_server.dto.request.AuthenticationRequest;
import com.mockproject.notary_admin_server.dto.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse login(AuthenticationRequest request);

}
