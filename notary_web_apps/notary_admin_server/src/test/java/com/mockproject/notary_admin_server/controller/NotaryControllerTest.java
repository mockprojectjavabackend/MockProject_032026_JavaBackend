package com.mockproject.notary_admin_server.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mockproject.notary_admin_server.dto.ApiSuccessResponse;
import com.mockproject.notary_admin_server.dto.request.UpdateNotaryInfoRequest;
import com.mockproject.notary_admin_server.dto.response.NotaryAdminResponse;
import com.mockproject.notary_admin_server.dto.response.NotaryBaseResponse;
import com.mockproject.notary_admin_server.service.impl.NotaryServiceImpl;

/**
 * NotaryControllerTest
 *
 * @version 1.0
 *
 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 02-04-2026      PhamTam     create
 */
@ExtendWith(MockitoExtension.class)
class NotaryControllerTest {

    @Mock
    private NotaryServiceImpl notaryService;

    @InjectMocks
    private NotaryController notaryController;

    // ================= GET PERSONAL INFO =================

    @Test
    void getPersonalInfoAsOwnerSuccess() {
        UUID notaryId = UUID.randomUUID();
        NotaryBaseResponse mockResponse =  NotaryAdminResponse.builder().build();

        when(notaryService.getPersonalInfo(notaryId)).thenReturn(mockResponse);

        ResponseEntity<ApiSuccessResponse<?>> result = notaryController.getPersonalInfo(notaryId);

        System.out.println("[getPersonalInfoAsOwnerSuccess] status : " + result.getStatusCode());
        System.out.println("[getPersonalInfoAsOwnerSuccess] body   : " + result.getBody());

        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        verify(notaryService).getPersonalInfo(notaryId);
    }

    // ================= UPDATE PERSONAL INFO =================

    @Test
    void updatePersonalInfoAsAdminSuccess() {
        UUID notaryId = UUID.randomUUID();
        UpdateNotaryInfoRequest request = new UpdateNotaryInfoRequest();
        request.setAddress("123 Main St");
        request.setCity("Houston");
        request.setZipCode("77001");

        NotaryBaseResponse mockResponse =  NotaryAdminResponse.builder().build();

        when(notaryService.updatePersonalInfo(notaryId, request)).thenReturn(mockResponse);

        ResponseEntity<ApiSuccessResponse<?>> result = notaryController.updatePersonalInfo(notaryId, request);

        System.out.println("[updatePersonalInfoAsAdminSuccess] status : " + result.getStatusCode());
        System.out.println("[updatePersonalInfoAsAdminSuccess] body   : " + result.getBody());

        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        verify(notaryService).updatePersonalInfo(notaryId, request);
    }

    @Test
    void updatePersonalInfoServiceError() {
        UUID notaryId = UUID.randomUUID();
        UpdateNotaryInfoRequest request = new UpdateNotaryInfoRequest();

        when(notaryService.updatePersonalInfo(notaryId, request))
                .thenThrow(new RuntimeException("Service error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> notaryController.updatePersonalInfo(notaryId, request));

        System.out.println("[updatePersonalInfoServiceError] error : " + ex.getMessage());

        assertEquals("Service error", ex.getMessage());
    }
}
