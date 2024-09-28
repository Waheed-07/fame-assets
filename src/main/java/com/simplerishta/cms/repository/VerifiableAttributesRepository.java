package com.simplerishta.cms.repository;

import com.simplerishta.cms.domain.VerifiableAttributes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VerifiableAttributes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VerifiableAttributesRepository extends JpaRepository<VerifiableAttributes, Long> {}
