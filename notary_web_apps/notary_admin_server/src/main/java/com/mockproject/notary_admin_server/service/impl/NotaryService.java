package com.mockproject.notary_admin_server.service.impl;

import com.mockproject.notary_admin_server.repository.NotariesRepository;
import com.mockproject.notary_common.entity.notary.Notary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NotaryService {
    private final NotariesRepository notariesRepository;

    public Set<Notary> getAllNotaries() {
        return new HashSet<>(notariesRepository.findAll()) ;
    }
}
