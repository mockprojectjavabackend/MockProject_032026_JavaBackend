package com.mockproject.notary_admin_server.controller;

import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mockproject.notary_admin_server.dto.ApiSuccessResponse;
import com.mockproject.notary_admin_server.dto.response.StateResponse;
import com.mockproject.notary_admin_server.service.impl.StateServiceImpl;

/**
 * StateControllerTest
 *
 * @version 1.0
 *
 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 02-04-2026      PhamTam     create
 */
@ExtendWith(MockitoExtension.class)
class StateControllerTest {

    @Mock
    private StateServiceImpl stateService;

    @InjectMocks
    private StateController stateController;

    // ================= HELPER =================

    private StateResponse createStateResponse(String name) {
        StateResponse response = new StateResponse();
        response.setStateName(name);
        return response;
    }

    // ================= GET ALL STATES =================

    @Test
    void getAllStatesSuccess() {
        List<StateResponse> mockStates = List.of(
                createStateResponse("Texas"),
                createStateResponse("Florida")
        );

        when(stateService.getAllStates()).thenReturn(mockStates);

        ResponseEntity<ApiSuccessResponse<List<StateResponse>>> result = stateController.getAllStates();

        System.out.println("[getAllStatesSuccess] status     : " + result.getStatusCode());
        System.out.println("[getAllStatesSuccess] total      : " + result.getBody().getData().size());
        result.getBody().getData().forEach(s ->
                System.out.println("[getAllStatesSuccess] state : " + s.getStateName()));

        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertEquals(2, result.getBody().getData().size());
        assertEquals("Texas", result.getBody().getData().get(0).getStateName());
        assertEquals("Florida", result.getBody().getData().get(1).getStateName());
        verify(stateService).getAllStates();
    }

    @Test
    void getAllStatesReturnsEmptyList() {
        when(stateService.getAllStates()).thenReturn(Collections.emptyList());

        ResponseEntity<ApiSuccessResponse<List<StateResponse>>> result = stateController.getAllStates();

        System.out.println("[getAllStatesReturnsEmptyList] status : " + result.getStatusCode());
        System.out.println("[getAllStatesReturnsEmptyList] total  : " + result.getBody().getData().size());

        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertTrue(result.getBody().getData().isEmpty());
        verify(stateService).getAllStates();
    }

    @Test
    void getAllStatesServiceError() {
        // simulate service error
        when(stateService.getAllStates()).thenThrow(new RuntimeException("Service error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> stateController.getAllStates());

        System.out.println("[getAllStatesServiceError] error : " + ex.getMessage());

        assertEquals("Service error", ex.getMessage());
    }
}