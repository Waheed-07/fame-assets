package com.simplerishta.cms.service.mapper;

import com.simplerishta.cms.domain.ApplicationConfigurations;
import com.simplerishta.cms.service.dto.ApplicationConfigurationsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ApplicationConfigurations} and its DTO {@link ApplicationConfigurationsDTO}.
 */
@Mapper(componentModel = "spring")
public interface ApplicationConfigurationsMapper extends EntityMapper<ApplicationConfigurationsDTO, ApplicationConfigurations> {}
