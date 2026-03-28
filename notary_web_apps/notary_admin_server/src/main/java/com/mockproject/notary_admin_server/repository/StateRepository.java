package com.mockproject.notary_admin_server.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mockproject.notary_common.entity.State;

@Repository
public interface StateRepository extends JpaRepository<State, UUID> {

}
