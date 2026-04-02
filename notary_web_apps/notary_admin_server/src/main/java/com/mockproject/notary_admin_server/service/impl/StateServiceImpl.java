package com.mockproject.notary_admin_server.service.impl;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

import com.mockproject.notary_admin_server.dto.response.StateResponse;
import com.mockproject.notary_admin_server.mapper.StateMapper;
import com.mockproject.notary_admin_server.repository.StateRepository;
import com.mockproject.notary_common.entity.State;
import com.mockproject.notary_admin_server.service.StateService;

/**
 * StateServiceImpl
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 02-04-2026      PhamTam      edit
 */
@Service
public class StateServiceImpl implements StateService {

    private final StateRepository stateRepository;
    private final StateMapper stateMapper;

    public StateServiceImpl(StateRepository stateRepository1, StateMapper stateMapper) {
        this.stateRepository = stateRepository1;
        this.stateMapper = stateMapper;
    }

    @Override
    public List<StateResponse> getAllStates() {
        List<State> states = stateRepository.findAll();
        return  stateMapper.toStateResponseList(states);
    }

    @Override
    public List<StateResponse> getAllStatesByNotary(UUID notaryId) {
        List<State> states  = stateRepository.findAllByNotaryId(notaryId);
        return stateMapper.toStateResponseList(states);
    }

}

