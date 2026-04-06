package com.mockproject.notary_admin_server.service;

import java.util.Map;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.mockproject.notary_admin_server.exception.AppException;
import com.mockproject.notary_admin_server.exception.errorCode.DocumentErrorCode;
import com.mockproject.notary_admin_server.repository.NotaryDocumentRepository;
import com.mockproject.notary_common.entity.notary.NotaryDocument;



/**
 * NotaryDocumentServiceImpl
 *
 * @version 1.0
 *
 *          Modification Logs:
 *          DATE AUTHOR DESCRIPTION
 *          -----------------------------------------------
 *          29-03-2026 AXL24 create
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class NotaryDocumentAuthorizationService {

    private final NotaryDocumentRepository notaryDocumentRepository;


    /*
    * Business logic for retrieving a document owned by a notary.
    * */
    public NotaryDocument getDocumentOwnedBy(UUID notaryId, UUID docId) {
        NotaryDocument document = notaryDocumentRepository.findById(docId)
                .orElseThrow(() -> new AppException(
                        DocumentErrorCode.DOCUMENT_NOT_FOUND,
                        Map.of("id", docId)));
        //Ensure document belongs to the notary
        if (!document.getNotary().getId().equals(notaryId)) {
            throw new AppException(
                    DocumentErrorCode.DOCUMENT_DOES_NOT_BELONG_TO_NOTARY,
                    Map.of("id", docId, "notaryId", notaryId));
        }

        return document;
    }
}
