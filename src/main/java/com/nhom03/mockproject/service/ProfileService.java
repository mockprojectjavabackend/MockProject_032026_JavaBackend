package com.nhom03.mockproject.service;

import com.nhom03.mockproject.dto.ApiResponse;
import com.nhom03.mockproject.dto.request.CreateProfileRequest;
import com.nhom03.mockproject.dto.response.ProfileResponse;

import java.util.List;

/**
 * ProfileService
 *
 * This interface defines business logic methods for Profile.
 *
 * Version 1.0
 *
 * Date: 17-03-2026
 *
 * Modification Logs:
 * DATE         AUTHOR      DESCRIPTION
 * ----------------------------------------------------------
 * 17-03-2026   Quoc        Create
 */
public interface ProfileService {

    List<ProfileResponse> getAllProfiles();

    ProfileResponse getProfileById(Long id);

    ProfileResponse createProfile(CreateProfileRequest cpr);

    ProfileResponse updateProfile(Long id, CreateProfileRequest cpr);

    void deleteProfile(Long id);
}
