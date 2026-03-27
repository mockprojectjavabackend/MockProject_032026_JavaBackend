package com.mockproject.notary_admin_server.repository;

import com.mockproject.notary_common.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface LanguageRepository extends JpaRepository<Language, UUID> {
    Set<Language> findByLangNameIn(Set<String> langNames);
}
