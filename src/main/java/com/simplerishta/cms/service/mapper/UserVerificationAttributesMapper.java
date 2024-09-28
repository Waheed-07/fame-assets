package com.simplerishta.cms.service.mapper;

import com.simplerishta.cms.domain.UserVerificationAttributes;
import com.simplerishta.cms.service.dto.UserVerificationAttributesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserVerificationAttributes} and its DTO {@link UserVerificationAttributesDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserVerificationAttributesMapper extends EntityMapper<UserVerificationAttributesDTO, UserVerificationAttributes> {}
