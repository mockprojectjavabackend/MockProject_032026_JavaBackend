package com.mockproject.notary_admin_server.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mockproject.notary_common.entity.State;
import com.mockproject.notary_admin_server.dto.response.StateResponse;
import com.mockproject.notary_admin_server.mapper.StateMapper;
import com.mockproject.notary_admin_server.repository.StateRepository;
import com.mockproject.notary_admin_server.service.impl.StateServiceImpl;

/**
 * StateServiceImplTest
 *
 * @version 1.0
 *
 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 02-04-2026      PhamTam     create
 */
@ExtendWith(MockitoExtension.class)
class StateServiceImplTest {

    @Mock
    private StateRepository stateRepository;

    @Mock
    private StateMapper stateMapper;

    @InjectMocks
    private StateServiceImpl stateService;

    // ================= HELPER =================

    private State createState(UUID id, String name) {
        State state = new State();
        state.setId(id);
        state.setStateName(name);
        return state;
    }

    private StateResponse createStateResponse(UUID id, String name) {
        StateResponse response = new StateResponse();
        response.setStateId(id);
        response.setStateName(name);
        return response;
    }

    // ================= GET ALL STATES =================

    @Test
    void getAllStatesSuccess() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        List<State> states = List.of(createState(id1, "Texas"), createState(id2, "Florida"));
        List<StateResponse> expected = List.of(createStateResponse(id1, "Texas"), createStateResponse(id2, "Florida"));

        when(stateRepository.findAll()).thenReturn(states);
        when(stateMapper.toStateResponseList(states)).thenReturn(expected);

        List<StateResponse> result = stateService.getAllStates();

        System.out.println("[getAllStatesSuccess] result size : " + result.size());
        result.forEach(s -> System.out.println("[getAllStatesSuccess] state : " + s.getStateName()));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Texas", result.get(0).getStateName());
        assertEquals("Florida", result.get(1).getStateName());
        verify(stateRepository).findAll();
        verify(stateMapper).toStateResponseList(states);
    }

    @Test
    void getAllStatesReturnsEmptyList() {
        when(stateRepository.findAll()).thenReturn(Collections.emptyList());
        when(stateMapper.toStateResponseList(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<StateResponse> result = stateService.getAllStates();

        System.out.println("[getAllStatesReturnsEmptyList] result size : " + result.size());

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(stateRepository).findAll();
    }

    @Test
    void getAllStatesRepositoryError() {
        // simulate DB error
        when(stateRepository.findAll()).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> stateService.getAllStates());

        System.out.println("[getAllStatesRepositoryError] error : " + ex.getMessage());

        assertEquals("DB error", ex.getMessage());
    }

    // ================= GET ALL STATES BY NOTARY =================

    @Test
    void getAllStatesByNotarySuccess() {
        UUID notaryId = UUID.randomUUID();
        UUID stateId = UUID.randomUUID();
        List<State> states = List.of(createState(stateId, "Texas"));
        List<StateResponse> expected = List.of(createStateResponse(stateId, "Texas"));

        when(stateRepository.findAllByNotaryId(notaryId)).thenReturn(states);
        when(stateMapper.toStateResponseList(states)).thenReturn(expected);

        List<StateResponse> result = stateService.getAllStatesByNotary(notaryId);

        System.out.println("[getAllStatesByNotarySuccess] notaryId    : " + notaryId);
        System.out.println("[getAllStatesByNotarySuccess] result size : " + result.size());
        result.forEach(s -> System.out.println("[getAllStatesByNotarySuccess] state : " + s.getStateName()));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Texas", result.get(0).getStateName());
        verify(stateRepository).findAllByNotaryId(notaryId);
        verify(stateMapper).toStateResponseList(states);
    }

    @Test
    void getAllStatesByNotaryReturnsEmptyList() {
        UUID notaryId = UUID.randomUUID();

        when(stateRepository.findAllByNotaryId(notaryId)).thenReturn(Collections.emptyList());
        when(stateMapper.toStateResponseList(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<StateResponse> result = stateService.getAllStatesByNotary(notaryId);

        System.out.println("[getAllStatesByNotaryReturnsEmptyList] notaryId    : " + notaryId);
        System.out.println("[getAllStatesByNotaryReturnsEmptyList] result size : " + result.size());

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(stateRepository).findAllByNotaryId(notaryId);
    }

    @Test
    void getAllStatesByNotaryRepositoryError() {
        UUID notaryId = UUID.randomUUID();

        // simulate DB error
        when(stateRepository.findAllByNotaryId(notaryId)).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> stateService.getAllStatesByNotary(notaryId));

        System.out.println("[getAllStatesByNotaryRepositoryError] error : " + ex.getMessage());

        assertEquals("DB error", ex.getMessage());
    }
}