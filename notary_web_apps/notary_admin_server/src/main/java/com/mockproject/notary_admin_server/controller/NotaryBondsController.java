package com.mockproject.notary_admin_server.controller;

import java.util.Map;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.mockproject.notary_admin_server.dto.ApiSuccessResponse;
import com.mockproject.notary_admin_server.dto.request.CreateNotaryBondRequest;
import com.mockproject.notary_admin_server.dto.response.NotaryBondResponse;
import com.mockproject.notary_admin_server.service.NotaryBondsService;
import com.mockproject.notary_common.entity.notary.NotaryBonds;

@RestController
@RequestMapping("/api/notaries")
public class NotaryBondsController {

    private final NotaryBondsService notaryBondsService;

    public NotaryBondsController(NotaryBondsService notaryBondsService) {
        this.notaryBondsService = notaryBondsService;
    }

    @GetMapping("/{id}/bonds")
    public ResponseEntity<ApiSuccessResponse<List<NotaryBondResponse>>> getAllBonds(@PathVariable UUID id) {
        List<NotaryBondResponse> bonds = notaryBondsService.getAllBondsByNotaryId(id).stream()
                .map(NotaryBondResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(ApiSuccessResponse.ok(bonds));
    }

    @GetMapping("/{id}/bond")
    public ResponseEntity<ApiSuccessResponse<NotaryBondResponse>> getBond(@PathVariable UUID id) {
        NotaryBonds bond = notaryBondsService.getBondByNotaryId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notary bond not found"));

        return ResponseEntity.ok(ApiSuccessResponse.ok(NotaryBondResponse.fromEntity(bond)));
    }

    @PostMapping("/{id}/bond")
    public ResponseEntity<ApiSuccessResponse<NotaryBondResponse>> createBond(@PathVariable UUID id,
            @Valid @RequestBody CreateNotaryBondRequest request) {
        NotaryBonds createdBond = notaryBondsService.createBond(
                id,
                request.providerName(),
                request.bondAmount(),
                request.effectiveDate(),
                request.expirationDate(),
                request.fileUrl());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiSuccessResponse.created(NotaryBondResponse.fromEntity(createdBond)));
    }

    @PutMapping("/{id}/bond")
    public ResponseEntity<ApiSuccessResponse<NotaryBondResponse>> updateBond(@PathVariable UUID id,
            @Valid @RequestBody CreateNotaryBondRequest request) {
        NotaryBonds updatedBond = notaryBondsService.updateBond(
                id,
                request.providerName(),
                request.bondAmount(),
                request.effectiveDate(),
                request.expirationDate(),
                request.fileUrl());

        return ResponseEntity.ok(ApiSuccessResponse.ok(NotaryBondResponse.fromEntity(updatedBond)));
    }

    @PostMapping(value = "/{id}/bond/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiSuccessResponse<Map<String, String>>> uploadBond(@PathVariable UUID id,
            @RequestParam("file") MultipartFile file) {
        NotaryBonds updatedBond = notaryBondsService.uploadBondFile(id, file);

        return ResponseEntity.ok(ApiSuccessResponse.ok(Map.of("file_url", updatedBond.getFileUrl())));
    }

}
