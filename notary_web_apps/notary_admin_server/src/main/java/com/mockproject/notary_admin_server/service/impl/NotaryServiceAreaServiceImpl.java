package com.mockproject.notary_admin_server.service.impl;

import java.time.LocalDateTime;
import java.util.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.mockproject.notary_common.entity.State;
import com.mockproject.notary_common.entity.notary.Notary;
import com.mockproject.notary_common.entity.notary.NotaryServiceArea;

import com.mockproject.notary_admin_server.repository.NotaryServiceAreaRepository;
import com.mockproject.notary_admin_server.service.NotaryServiceAreaService;

/**
 * NotaryServiceAreaServiceImpl
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 27-03-2026      PhamTam      create
 * 02-04-2026      PhamTam      edit
 */

@Service
public class NotaryServiceAreaServiceImpl implements NotaryServiceAreaService {
    private final NotaryServiceAreaRepository notaryServiceAreaRepository;

    public NotaryServiceAreaServiceImpl(NotaryServiceAreaRepository notaryServiceAreaRepository) {
        this.notaryServiceAreaRepository = notaryServiceAreaRepository;
    }

    /**
     * Update service area states for a notary (soft delete / restore / insert)
     *
     * @param notaryId    notary UUID
     * @param newStateIds new list of state UUIDs
     */
    @Transactional
    @Override
    public void updateStates(UUID notaryId, List<UUID> newStateIds) {
        if (notaryId == null) {
            throw new IllegalArgumentException("Notary ID cannot be null");
        }
        if (newStateIds == null) {
            newStateIds = List.of();
        }
        //Soft delete states that are no longer in the new list
        notaryServiceAreaRepository.softDeleteExcluding(notaryId, newStateIds, LocalDateTime.now());

        // Restore soft-deleted states that are back in the new list
        notaryServiceAreaRepository.restoreByStateIds(notaryId, newStateIds);

        // Insert new
        List<UUID> existingIds = notaryServiceAreaRepository.findStateIdsByNotaryId(notaryId);
        List<NotaryServiceArea> toInsert = newStateIds.stream()
                .filter(id -> !existingIds.contains(id))
                .map(id -> NotaryServiceArea.builder()
                        .notary(Notary.builder().id(notaryId).build())
                        .state(State.builder().id(id).build())
                        .build())
                .toList();

        notaryServiceAreaRepository.saveAll(toInsert);
    }

}
