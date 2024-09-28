package com.simplerishta.cms.service.mapper;

import com.simplerishta.cms.domain.VerifiableAttributes;
import com.simplerishta.cms.service.dto.VerifiableAttributesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VerifiableAttributes} and its DTO {@link VerifiableAttributesDTO}.
 */
@Mapper(componentModel = "spring")
public interface VerifiableAttributesMapper extends EntityMapper<VerifiableAttributesDTO, VerifiableAttributes> {}
