package com.mockproject.notary_admin_server.service;

import com.mockproject.notary_admin_server.dto.request.*;
import com.mockproject.notary_admin_server.dto.response.AuthenticationResponse;
import com.mockproject.notary_admin_server.dto.response.UserResponse;

public interface AuthenticationService {
    AuthenticationResponse login(AuthenticationRequest request);

    AuthenticationResponse refreshToken(String refreshToken);

    void logout(String refreshToken);

    void logoutAll();

//    void verifyUser(VerifyUserRequest request);
//
//    void forgotPassword(ForgotPasswordRequest request);
//
//    void resetPassword(ResetPasswordRequest request);

    void setPassword(SetPasswordRequest request);
}
