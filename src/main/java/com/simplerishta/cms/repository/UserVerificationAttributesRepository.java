package com.simplerishta.cms.repository;

import com.simplerishta.cms.domain.UserVerificationAttributes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserVerificationAttributes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserVerificationAttributesRepository extends JpaRepository<UserVerificationAttributes, Long> {}
