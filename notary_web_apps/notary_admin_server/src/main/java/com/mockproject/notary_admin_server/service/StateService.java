package com.mockproject.notary_admin_server.service;

import com.mockproject.notary_admin_server.dto.response.StateResponse;
import com.mockproject.notary_admin_server.mapper.StateMapper;
import com.mockproject.notary_admin_server.repository.StateRepository;
import com.mockproject.notary_common.entity.State;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StateService {

    private final StateRepository stateRepository;
    private final StateMapper stateMapper;

    public StateService(StateRepository stateRepository, StateRepository stateRepository1, StateMapper stateMapper) {
        this.stateRepository = stateRepository1;
        this.stateMapper = stateMapper;
    }

    public List<StateResponse> getAllState()
    {
        List<State> states = stateRepository.findAll();
        return  stateMapper.toStateResponseList(states);
    }

    List<StateResponse> getAllStateByNotary(UUID notaryId){

        List<State> states  = stateRepository.findAllByNotaryId(notaryId);
        return stateMapper.toStateResponseList(states);

    }
}
