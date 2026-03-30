package com.mockproject.notary_admin_server.repository;

import com.mockproject.notary_common.entity.notary.Notary;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

/**
 * NotarySpecification
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 27-03-2026      TranMinh    create
 */
public class NotarySpecification {
    public static Specification<Notary> filter(String status, String state, String serviceType, String search) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (search != null && !search.isBlank()) {
                String likePattern = "%" + search.toLowerCase() + "%";
                predicates = cb.and(predicates, cb.or(
                        cb.like(cb.lower(root.get("fullName")), likePattern),
                        cb.like(cb.lower(root.get("email")), likePattern)
                ));
            }

            if (status != null && !status.isBlank()) {
                predicates = cb.and(predicates, cb.equal(root.get("status"), status.toUpperCase()));
            }

            if (state != null && !state.isBlank()) {
                var commissionsJoin = root.join("commissions", JoinType.INNER);
                predicates = cb.and(predicates, cb.equal(commissionsJoin.get("commissionState"), state));
            }

            if (serviceType != null && !serviceType.isBlank()) {
                var capabilityJoin = root.join("capability", JoinType.INNER);
                switch (serviceType.toUpperCase()) {
                    case "MOBILE" -> predicates = cb.and(predicates, cb.isTrue(capabilityJoin.get("mobile")));
                    case "RON" -> predicates = cb.and(predicates, cb.isTrue(capabilityJoin.get("ron")));
                    case "LOAN SIGNING" -> predicates = cb.and(predicates, cb.isTrue(capabilityJoin.get("loanSigning")));
                }
            }

            query.distinct(true);
            return predicates;
        };
    }
}
