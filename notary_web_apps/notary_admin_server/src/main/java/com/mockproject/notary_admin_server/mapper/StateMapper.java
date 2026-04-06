package com.mockproject.notary_admin_server.mapper;

import java.util.List;
import org.springframework.stereotype.Component;

import com.mockproject.notary_admin_server.dto.response.StateResponse;
import com.mockproject.notary_common.entity.State;

/**
 * StateMapper
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 02-04-2026      PhamTam      edit
 */
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
