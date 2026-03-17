package com.nhom03.mockproject.controller;

import java.util.List;

import com.nhom03.mockproject.dto.ApiResponse;
import com.nhom03.mockproject.dto.request.CreateProfileRequest;
import com.nhom03.mockproject.dto.response.ProfileResponse;
import com.nhom03.mockproject.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ProfileController
 *
 * This class handles REST APIs for managing Profile data.
 *
 * Version 1.0
 *
 * Date: 17-03-2026
 *
 * Copyright
 *
 * Modification Logs:
 * DATE         AUTHOR      DESCRIPTION
 * ----------------------------------------------------------
 * 17-03-2026   Quoc        Create
 */
@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    /**
     * Get all profiles
     *
     * @return ResponseEntity containing ApiResponse with list of ProfileResponse
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProfileResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(profileService.getAllProfiles()));
    }

    /**
     * Get profile by ID
     *
     * @param id profile ID
     * @return ResponseEntity containing ApiResponse with ProfileResponse
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProfileResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(profileService.getProfileById(id)));
    }

    /**
     * Create a new profile
     *
     * @param dto request body containing profile data
     * @return ResponseEntity containing ApiResponse with created ProfileResponse
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> create(@RequestBody CreateProfileRequest dto) {
        return ResponseEntity.status(201)
                .body(ApiResponse.success("Create success", profileService.createProfile(dto)));
    }

    /**
     * Update an existing profile
     *
     * @param id profile ID
     * @param dto request body containing updated profile data
     * @return ResponseEntity containing ApiResponse with updated ProfileResponse
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProfileResponse>> update(@PathVariable Long id,
                                                          @RequestBody CreateProfileRequest dto) {
        return ResponseEntity.ok(
                ApiResponse.success("Update success", profileService.updateProfile(id, dto))
        );
    }

    /**
     * Delete profile by ID
     *
     * @param id profile ID
     * @return ResponseEntity with HTTP status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        profileService.deleteProfile(id);
        return ResponseEntity.noContent().build(); // 204
    }

}
