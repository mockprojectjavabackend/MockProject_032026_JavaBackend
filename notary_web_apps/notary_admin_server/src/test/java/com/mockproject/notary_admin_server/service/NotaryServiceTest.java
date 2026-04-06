package com.mockproject.notary_admin_server.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mockproject.notary_admin_server.service.impl.NotaryServiceImpl;
import com.mockproject.notary_admin_server.service.impl.NotaryServiceAreaServiceImpl;
import com.mockproject.notary_admin_server.service.impl.StateServiceImpl;

import com.mockproject.notary_common.constant.EmploymentType;
import com.mockproject.notary_admin_server.dto.request.UpdateNotaryInfoRequest;
import com.mockproject.notary_admin_server.dto.response.NotaryAdminResponse;
import com.mockproject.notary_admin_server.dto.response.NotaryBaseResponse;
import com.mockproject.notary_admin_server.dto.response.NotaryPublicResponse;
import com.mockproject.notary_admin_server.dto.response.StateResponse;
import com.mockproject.notary_admin_server.mapper.NotaryMapper;
import com.mockproject.notary_admin_server.repository.NotaryRepository;
import com.mockproject.notary_common.entity.notary.Notary;
import com.mockproject.notary_common.entity.User;

/**
 * NotaryServiceImplTest
 *
 * @version 1.0
 *
 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 02-04-2026      PhamTam     create
 */
@ExtendWith(MockitoExtension.class)
class NotaryServiceImplTest {

    @Mock
    private NotaryRepository notaryRepository;

    @Mock
    private NotaryMapper notaryMapper;

    @Mock
    private NotaryServiceAreaServiceImpl notaryServiceAreaService;

    @Mock
    private StateServiceImpl stateService;

    @InjectMocks
    private NotaryServiceImpl notaryService;

    // ================= HELPER =================

    private User createUser(UUID userId) {
        User user = new User();
        user.setId(userId);
        return user;
    }

    private Notary createNotary(UUID id) {
        Notary n = new Notary();
        n.setId(id);
        n.setFullName("Pham Tam");
        n.setPhone("0909123456");
        n.setSsn("123-45-6789");
        n.setPhotoUrl("https://example.com/photo.jpg");
        n.setDateOfBirth(LocalDate.of(1995, 6, 15));
        n.setStartDate(LocalDate.of(2020, 1, 1));
        n.setAddress("123 Main St");
        n.setCity("Houston");
        n.setZipCode("77001");
        n.setEmploymentType(EmploymentType.valueOf("FULL_TIME"));
        n.setInternalNotes("Good performance");
        n.setCreatedAt(LocalDateTime.now());
        n.setUpdatedAt(LocalDateTime.now());
        n.setUser(createUser(UUID.randomUUID()));
        return n;
    }

    private List<StateResponse> createStates() {
        StateResponse state = new StateResponse();
        state.setStateId(UUID.randomUUID());
        state.setStateName("Texas");
        return List.of(state);
    }

    private NotaryPublicResponse createPublicResponse(Notary n) {
        return NotaryPublicResponse.builder()
                .id(n.getId())
                .fullName(n.getFullName())
                .phone(n.getPhone())
                .photoUrl(n.getPhotoUrl())
                .dateOfBirth(n.getDateOfBirth())
                .startDate(n.getStartDate())
                .address(n.getAddress())
                .city(n.getCity())
                .zipCode(n.getZipCode())
                .userId(n.getUser().getId())
                .createdAt(n.getCreatedAt())
                .updatedAt(n.getUpdatedAt())
                .build();
    }

    private NotaryAdminResponse createAdminResponse(Notary n) {
        return NotaryAdminResponse.builder()
                .id(n.getId())
                .fullName(n.getFullName())
                .phone(n.getPhone())
                .ssn(n.getSsn())
                .photoUrl(n.getPhotoUrl())
                .dateOfBirth(n.getDateOfBirth())
                .startDate(n.getStartDate())
                .address(n.getAddress())
                .city(n.getCity())
                .zipCode(n.getZipCode())
                .employmentType(n.getEmploymentType())
                .internalNotes(n.getInternalNotes())
                .userId(n.getUser().getId())
                .createdAt(n.getCreatedAt())
                .updatedAt(n.getUpdatedAt())
                .build();
    }

    // ================= GET PERSONAL INFO =================

    @Test
    void getPersonalInfoAsPublicSuccess() {
        UUID id = UUID.randomUUID();
        Notary notary = createNotary(id);

        when(notaryRepository.findById(id)).thenReturn(Optional.of(notary));
        when(stateService.getAllStatesByNotary(id)).thenReturn(createStates());
        when(notaryMapper.toPublicResponse(any(), any())).thenReturn(createPublicResponse(notary));

        NotaryBaseResponse result = notaryService.getPersonalInfo(id, false);

        assertNotNull(result);
        assertInstanceOf(NotaryPublicResponse.class, result);
        assertEquals(notary.getFullName(), result.getFullName());
        verify(notaryMapper).toPublicResponse(any(), any());
        verify(notaryMapper, never()).toAdminResponse(any(), any());
    }

    @Test
    void getPersonalInfoAsAdminSuccess() {
        UUID id = UUID.randomUUID();
        Notary notary = createNotary(id);

        when(notaryRepository.findById(id)).thenReturn(Optional.of(notary));
        when(stateService.getAllStatesByNotary(id)).thenReturn(createStates());
        when(notaryMapper.toAdminResponse(any(), any())).thenReturn(createAdminResponse(notary));

        NotaryBaseResponse result = notaryService.getPersonalInfo(id, true);

        assertNotNull(result);
        assertInstanceOf(NotaryAdminResponse.class, result);
        assertEquals(notary.getSsn(), ((NotaryAdminResponse) result).getSsn());
        assertEquals(notary.getEmploymentType(), ((NotaryAdminResponse) result).getEmploymentType());
        verify(notaryMapper).toAdminResponse(any(), any());
        verify(notaryMapper, never()).toPublicResponse(any(), any());
    }

    @Test
    void getPersonalInfoNotFound() {
        UUID id = UUID.randomUUID();

        when(notaryRepository.findById(id)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> notaryService.getPersonalInfo(id, false));

        assertEquals("Notary not found", ex.getReason());
    }

    // ================= UPDATE PERSONAL INFO =================

    @Test
    void updatePersonalInfoAsPublicSuccess() {
        UUID id = UUID.randomUUID();
        Notary notary = createNotary(id);

        UpdateNotaryInfoRequest request = new UpdateNotaryInfoRequest();
        request.setAddress("456 New St");
        request.setCity("Dallas");
        request.setZipCode("75001");
        request.setPhotoUrl("https://example.com/new-photo.jpg");

        when(notaryRepository.findById(id)).thenReturn(Optional.of(notary));
        when(notaryRepository.save(any())).thenReturn(notary);
        when(stateService.getAllStatesByNotary(id)).thenReturn(createStates());
        when(notaryMapper.toPublicResponse(any(), any())).thenReturn(createPublicResponse(notary));

        NotaryBaseResponse result = notaryService.updatePersonalInfo(id, request, false);

        assertNotNull(result);
        assertInstanceOf(NotaryPublicResponse.class, result);
        assertEquals("456 New St", notary.getAddress());
        assertEquals("Dallas", notary.getCity());
        assertEquals("75001", notary.getZipCode());
        verify(notaryRepository).save(notary);
        verify(notaryMapper).toPublicResponse(any(), any());
        verify(notaryMapper, never()).toAdminResponse(any(), any());
    }

    @Test
    void updatePersonalInfoAsAdminSuccess() {
        UUID id = UUID.randomUUID();
        Notary notary = createNotary(id);

        UpdateNotaryInfoRequest request = new UpdateNotaryInfoRequest();
        request.setFullName("Nguyen Van A");
        request.setPhone("0911222333");
        request.setSsn("987-65-4321");
        request.setEmploymentType(EmploymentType.valueOf("PART_TIME"));
        request.setInternalNotes("Updated notes");
        request.setAddress("789 Admin Rd");
        request.setCity("Austin");
        request.setZipCode("78701");

        when(notaryRepository.findById(id)).thenReturn(Optional.of(notary));
        when(notaryRepository.save(any())).thenReturn(notary);
        when(stateService.getAllStatesByNotary(id)).thenReturn(createStates());
        when(notaryMapper.toAdminResponse(any(), any())).thenReturn(createAdminResponse(notary));

        NotaryBaseResponse result = notaryService.updatePersonalInfo(id, request, true);

        assertNotNull(result);
        assertInstanceOf(NotaryAdminResponse.class, result);
        assertEquals("789 Admin Rd", notary.getAddress());
        assertEquals("Austin", notary.getCity());
        assertEquals("78701", notary.getZipCode());
        assertEquals("Nguyen Van A", notary.getFullName());
        assertEquals("0911222333", notary.getPhone());
        assertEquals("987-65-4321", notary.getSsn());
        assertEquals(EmploymentType.valueOf("PART_TIME"), notary.getEmploymentType());
        assertEquals("Updated notes", notary.getInternalNotes());
        verify(notaryRepository).save(notary);
        verify(notaryMapper).toAdminResponse(any(), any());
        verify(notaryMapper, never()).toPublicResponse(any(), any());
    }

    @Test
    void updatePersonalInfoNotFound() {
        UUID id = UUID.randomUUID();

        when(notaryRepository.findById(id)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> notaryService.updatePersonalInfo(id, new UpdateNotaryInfoRequest(), false));

        assertEquals("Notary not found", ex.getReason());
    }
}
