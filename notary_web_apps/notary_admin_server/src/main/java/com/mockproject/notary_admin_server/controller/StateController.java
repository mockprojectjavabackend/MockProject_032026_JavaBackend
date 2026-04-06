package com.mockproject.notary_admin_server.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mockproject.notary_admin_server.dto.ApiSuccessResponse;
import com.mockproject.notary_admin_server.dto.response.StateResponse;
import com.mockproject.notary_admin_server.service.impl.StateServiceImpl;

/**
 * StateController
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 30-03-2026      PhamTam      create
 */
@RestController
@Validated
@RequestMapping("/api/states")
public class StateController {

    private final StateServiceImpl stateService;

    public StateController(StateServiceImpl stateService) {
        this.stateService = stateService;
    }

    @GetMapping()
    public ResponseEntity<ApiSuccessResponse<List<StateResponse>>> getAllStates() {
        return ResponseEntity.ok(
                ApiSuccessResponse.ok(stateService.getAllStates())
        );
    }


}
