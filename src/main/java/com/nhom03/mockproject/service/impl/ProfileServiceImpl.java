package com.nhom03.mockproject.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.nhom03.mockproject.dto.request.CreateProfileRequest;
import com.nhom03.mockproject.dto.response.ProfileResponse;
import com.nhom03.mockproject.entity.Profile;
import com.nhom03.mockproject.repository.ProfileRepository;
import com.nhom03.mockproject.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ProfileServiceImpl
 *
 * Implementation of ProfileService.
 * Handles business logic for Profile operations.
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
@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    /**
     * Convert Profile entity to ProfileResponse DTO
     *
     * @param profile entity object from database
     * @return ProfileResponse object used for API response
     */
    private ProfileResponse convertToDTO(Profile profile) {
        return new ProfileResponse(
                profile.getId(),
                profile.getName(),
                profile.getEmail(),
                profile.getPhone()
        );
    }

    /**
     * Convert CreateProfileRequest DTO to Profile entity
     *
     * @param dto request data from client
     * @return Profile entity for saving to database
     */
    private Profile convertToEntity(CreateProfileRequest dto) {
        Profile p = new Profile();
        p.setId(dto.getId());
        p.setName(dto.getName());
        p.setEmail(dto.getEmail());
        p.setPhone(dto.getPhone());
        return p;
    }

    /**
     * Get all profiles from database
     * @return list of profile DTOs
     */
    @Override
    public List<ProfileResponse> getAllProfiles() {
        return profileRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    /**
     * Get profile by ID
     * @param id profile ID
     * @return profile data
     */
    @Override
    public ProfileResponse getProfileById(Long id) {
        Profile p = profileRepository.findById(id).orElseThrow();
        return convertToDTO(p);
    }

    /**
     * Create profile
     * @param dto profile data
     * @return created profile
     */
    @Override
    public ProfileResponse createProfile(CreateProfileRequest dto) {
        Profile profile = convertToEntity(dto);
        return convertToDTO(profileRepository.save(profile));
    }

    /**
     * Update profile
     * @param id profile ID
     * @param dto updated data
     * @return updated profile
     */
    @Override
    public ProfileResponse updateProfile(Long id, CreateProfileRequest dto) {
        Profile profile = profileRepository.findById(id).orElseThrow();

        profile.setName(dto.getName());
        profile.setEmail(dto.getEmail());
        profile.setPhone(dto.getPhone());

        return convertToDTO(profileRepository.save(profile));
    }

    /**
     * Delete profile
     * @param id profile ID
     */
    @Override
    public void deleteProfile(Long id) {
        profileRepository.deleteById(id);
    }
}
