package com.simplerishta.cms.service;

import com.simplerishta.cms.domain.UserVerificationAttributes;
import com.simplerishta.cms.repository.UserVerificationAttributesRepository;
import com.simplerishta.cms.service.dto.UserVerificationAttributesDTO;
import com.simplerishta.cms.service.mapper.UserVerificationAttributesMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.simplerishta.cms.domain.UserVerificationAttributes}.
 */
@Service
@Transactional
public class UserVerificationAttributesService {

    private static final Logger LOG = LoggerFactory.getLogger(UserVerificationAttributesService.class);

    private final UserVerificationAttributesRepository userVerificationAttributesRepository;

    private final UserVerificationAttributesMapper userVerificationAttributesMapper;

    public UserVerificationAttributesService(
        UserVerificationAttributesRepository userVerificationAttributesRepository,
        UserVerificationAttributesMapper userVerificationAttributesMapper
    ) {
        this.userVerificationAttributesRepository = userVerificationAttributesRepository;
        this.userVerificationAttributesMapper = userVerificationAttributesMapper;
    }

    /**
     * Save a userVerificationAttributes.
     *
     * @param userVerificationAttributesDTO the entity to save.
     * @return the persisted entity.
     */
    public UserVerificationAttributesDTO save(UserVerificationAttributesDTO userVerificationAttributesDTO) {
        LOG.debug("Request to save UserVerificationAttributes : {}", userVerificationAttributesDTO);
        UserVerificationAttributes userVerificationAttributes = userVerificationAttributesMapper.toEntity(userVerificationAttributesDTO);
        userVerificationAttributes = userVerificationAttributesRepository.save(userVerificationAttributes);
        return userVerificationAttributesMapper.toDto(userVerificationAttributes);
    }

    /**
     * Update a userVerificationAttributes.
     *
     * @param userVerificationAttributesDTO the entity to save.
     * @return the persisted entity.
     */
    public UserVerificationAttributesDTO update(UserVerificationAttributesDTO userVerificationAttributesDTO) {
        LOG.debug("Request to update UserVerificationAttributes : {}", userVerificationAttributesDTO);
        UserVerificationAttributes userVerificationAttributes = userVerificationAttributesMapper.toEntity(userVerificationAttributesDTO);
        userVerificationAttributes = userVerificationAttributesRepository.save(userVerificationAttributes);
        return userVerificationAttributesMapper.toDto(userVerificationAttributes);
    }

    /**
     * Partially update a userVerificationAttributes.
     *
     * @param userVerificationAttributesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserVerificationAttributesDTO> partialUpdate(UserVerificationAttributesDTO userVerificationAttributesDTO) {
        LOG.debug("Request to partially update UserVerificationAttributes : {}", userVerificationAttributesDTO);

        return userVerificationAttributesRepository
            .findById(userVerificationAttributesDTO.getId())
            .map(existingUserVerificationAttributes -> {
                userVerificationAttributesMapper.partialUpdate(existingUserVerificationAttributes, userVerificationAttributesDTO);

                return existingUserVerificationAttributes;
            })
            .map(userVerificationAttributesRepository::save)
            .map(userVerificationAttributesMapper::toDto);
    }

    /**
     * Get all the userVerificationAttributes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserVerificationAttributesDTO> findAll() {
        LOG.debug("Request to get all UserVerificationAttributes");
        return userVerificationAttributesRepository
            .findAll()
            .stream()
            .map(userVerificationAttributesMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one userVerificationAttributes by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserVerificationAttributesDTO> findOne(Long id) {
        LOG.debug("Request to get UserVerificationAttributes : {}", id);
        return userVerificationAttributesRepository.findById(id).map(userVerificationAttributesMapper::toDto);
    }

    /**
     * Delete the userVerificationAttributes by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete UserVerificationAttributes : {}", id);
        userVerificationAttributesRepository.deleteById(id);
    }
}
