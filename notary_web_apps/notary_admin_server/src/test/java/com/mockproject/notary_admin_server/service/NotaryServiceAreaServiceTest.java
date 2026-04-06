package com.mockproject.notary_admin_server.service;

import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mockproject.notary_admin_server.repository.NotaryServiceAreaRepository;
import com.mockproject.notary_admin_server.service.impl.NotaryServiceAreaServiceImpl;

/**
 * NotaryServiceAreaServiceImplTest
 *
 * @version 1.0
 *
 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 02-04-2026      PhamTam     create
 */
@ExtendWith(MockitoExtension.class)
class NotaryServiceAreaServiceImplTest {

    @Mock
    private NotaryServiceAreaRepository repository;

    @InjectMocks
    private NotaryServiceAreaServiceImpl service;

    // ================= UPDATE STATES =================

    @Test
    void updateStatesSuccess() {
        UUID notaryId = UUID.randomUUID();
        UUID state1 = UUID.randomUUID();
        UUID state2 = UUID.randomUUID();
        List<UUID> newStates = List.of(state1, state2);
        List<UUID> existingStates = List.of(UUID.randomUUID()); // 1 old state not in newStates

        when(repository.findStateIdsByNotaryId(notaryId)).thenReturn(existingStates);

        service.updateStates(notaryId, newStates);
        verify(repository).softDeleteExcluding(eq(notaryId), eq(newStates), any());
        verify(repository).restoreByStateIds(notaryId, newStates);
        verify(repository).findStateIdsByNotaryId(notaryId);
        verify(repository).saveAll(any()); // state1 and state2 are both new → insert both
        System.out.println("[updateStatesSuccess] existingStates : " + existingStates);
        System.out.println("[updateStatesSuccess] newStates      : " + newStates);
    }

    @Test
    void updateStatesWithEmptyNewStates() {
        UUID notaryId = UUID.randomUUID();
        List<UUID> existingStates = List.of(UUID.randomUUID());

        when(repository.findStateIdsByNotaryId(notaryId)).thenReturn(existingStates);

        service.updateStates(notaryId, Collections.emptyList());

        System.out.println("[updateStatesWithEmptyNewStates] newStates = []");

        verify(repository).softDeleteExcluding(eq(notaryId), eq(Collections.emptyList()), any());
        verify(repository).restoreByStateIds(notaryId, Collections.emptyList());
        verify(repository).saveAll(any()); // no new states → saveAll with empty list
    }

    @Test
    void updateStatesWithNullNewStates() {
        UUID notaryId = UUID.randomUUID();

        when(repository.findStateIdsByNotaryId(notaryId)).thenReturn(Collections.emptyList());

        // null is converted to List.of() inside service
        service.updateStates(notaryId, null);

        System.out.println("[updateStatesWithNullNewStates] newStates = null → treated as []");

        verify(repository).softDeleteExcluding(eq(notaryId), eq(Collections.emptyList()), any());
        verify(repository).restoreByStateIds(notaryId, Collections.emptyList());
        verify(repository).saveAll(any()); // empty list saved
    }

    @Test
    void updateStatesWithNullNotaryId() {
        // null notaryId → service must throw IllegalArgumentException
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.updateStates(null, List.of(UUID.randomUUID())));

        System.out.println("[updateStatesWithNullNotaryId] error : " + ex.getMessage());

        assertEquals("Notary ID cannot be null", ex.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void updateStatesSkipsAlreadyExistingStates() {
        UUID notaryId = UUID.randomUUID();
        UUID existingStateId = UUID.randomUUID();
        UUID newStateId = UUID.randomUUID();

        // existingStateId already in DB, newStateId is not
        List<UUID> newStates = List.of(existingStateId, newStateId);
        List<UUID> existingStates = List.of(existingStateId);

        when(repository.findStateIdsByNotaryId(notaryId)).thenReturn(existingStates);

        service.updateStates(notaryId, newStates);

        System.out.println("[updateStatesSkipsAlreadyExistingStates] existingStates : " + existingStates);
        System.out.println("[updateStatesSkipsAlreadyExistingStates] newStates      : " + newStates);

        verify(repository).saveAll(any()); // only insert newStateId, skip existingStateId
    }

    @Test
    void updateStatesRepositoryError() {
        UUID notaryId = UUID.randomUUID();

        // simulate DB error when querying existing states
        when(repository.findStateIdsByNotaryId(notaryId))
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.updateStates(notaryId, List.of(UUID.randomUUID())));

        System.out.println("[updateStatesRepositoryError] error : " + ex.getMessage());

        assertEquals("DB error", ex.getMessage());
    }
}