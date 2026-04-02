package com.mockproject.notary_admin_server.service;

public interface EmailService {
    void sendInvitationEmail(String toEmail, String fullName, String inviteLink);
}
