package com.mockproject.notary_admin_server.mapper;

import com.mockproject.notary_admin_server.dto.response.StateResponse;
import com.mockproject.notary_common.entity.State;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StateMapper {

    public StateResponse toStateResponse(State state) {
        if (state == null) {
            return null;
        }
        return StateResponse.builder()
                .stateId(state.getId())
                .stateName(state.getStateName())
                .stateCode(state.getStateCode())
                .build();
    }

    public List<StateResponse> toStateResponseList(List<State> states) {
        return states.stream()
                .map(this::toStateResponse)
                .toList();
    }

}
