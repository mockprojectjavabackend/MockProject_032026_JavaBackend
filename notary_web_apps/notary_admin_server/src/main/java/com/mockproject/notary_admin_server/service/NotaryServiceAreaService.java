package com.mockproject.notary_admin_server.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

import com.mockproject.notary_admin_server.repository.NotaryServiceAreaRepository;
import com.mockproject.notary_common.entity.State;
import com.mockproject.notary_common.entity.notary.Notary;
import com.mockproject.notary_common.entity.notary.NotaryServiceArea;


@Service
public class NotaryServiceAreaService {
    private final NotaryServiceAreaRepository notaryServiceAreaRepository;

    public NotaryServiceAreaService(NotaryServiceAreaRepository notaryServiceAreaRepository) {
        this.notaryServiceAreaRepository = notaryServiceAreaRepository;
    }

    @Transactional
    public void updateStates(UUID notaryId, List<UUID> newStateIds) {

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
