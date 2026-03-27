package com.mockproject.notary_admin_server.controller;

import java.util.Map;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.mockproject.notary_admin_server.dto.ApiResponse;
import com.mockproject.notary_admin_server.dto.request.CreateNotaryInsuranceRequest;
import com.mockproject.notary_admin_server.dto.response.NotaryInsuranceResponse;
import com.mockproject.notary_admin_server.service.NotaryInsuranceService;
import com.mockproject.notary_common.entity.notary.NotaryInsurance;

@RestController
@RequestMapping("/api/notaries")
public class NotaryInsuranceController {

    private final NotaryInsuranceService notaryInsuranceService;

    public NotaryInsuranceController(NotaryInsuranceService notaryInsuranceService) {
        this.notaryInsuranceService = notaryInsuranceService;
    }

    @GetMapping("/{id}/insurances")
    public ResponseEntity<ApiResponse<List<NotaryInsuranceResponse>>> getAllInsurances(@PathVariable UUID id) {
        List<NotaryInsuranceResponse> insurances = notaryInsuranceService.getAllInsurancesByNotaryId(id).stream()
                .map(NotaryInsuranceResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(ApiResponse.success(insurances));
    }

    @GetMapping("/{id}/insurance")
    public ResponseEntity<ApiResponse<NotaryInsuranceResponse>> getInsurance(@PathVariable UUID id) {
        NotaryInsurance insurance = notaryInsuranceService.getInsuranceByNotaryId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notary insurance not found"));

        return ResponseEntity.ok(ApiResponse.success(NotaryInsuranceResponse.fromEntity(insurance)));
    }

    @PostMapping("/{id}/insurance")
    public ResponseEntity<ApiResponse<NotaryInsuranceResponse>> createInsurance(@PathVariable UUID id,
            @Valid @RequestBody CreateNotaryInsuranceRequest request) {
        NotaryInsurance createdInsurance = notaryInsuranceService.createInsurance(
                id,
                request.providerName(),
                request.policyNumber(),
                request.coverageAmount(),
                request.expirationDate());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(NotaryInsuranceResponse.fromEntity(createdInsurance)));
    }

    @PutMapping("/{id}/insurance")
    public ResponseEntity<ApiResponse<NotaryInsuranceResponse>> updateInsurance(@PathVariable UUID id,
            @Valid @RequestBody CreateNotaryInsuranceRequest request) {
        NotaryInsurance updatedInsurance = notaryInsuranceService.updateInsurance(
                id,
                request.providerName(),
                request.policyNumber(),
                request.coverageAmount(),
                request.expirationDate());

        return ResponseEntity.ok(ApiResponse.success(NotaryInsuranceResponse.fromEntity(updatedInsurance)));
    }

    @PostMapping(value = "/{id}/insurance/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadInsurance(@PathVariable UUID id,
            @RequestParam("file") MultipartFile file) {
        NotaryInsurance updatedInsurance = notaryInsuranceService.uploadInsuranceFile(id, file);

        return ResponseEntity.ok(ApiResponse.success(Map.of("file_url", updatedInsurance.getFileUrl())));
    }
}