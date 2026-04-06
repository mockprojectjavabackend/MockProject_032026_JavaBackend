package com.mockproject.notary_admin_server.repository;

import com.mockproject.notary_common.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StateRepository extends JpaRepository<State, UUID> {
}
