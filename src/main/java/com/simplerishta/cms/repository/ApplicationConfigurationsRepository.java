package com.simplerishta.cms.repository;

import com.simplerishta.cms.domain.ApplicationConfigurations;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ApplicationConfigurations entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationConfigurationsRepository extends JpaRepository<ApplicationConfigurations, Long> {}
