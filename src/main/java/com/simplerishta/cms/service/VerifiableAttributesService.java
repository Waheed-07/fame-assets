package com.simplerishta.cms.service;

import com.simplerishta.cms.domain.VerifiableAttributes;
import com.simplerishta.cms.repository.VerifiableAttributesRepository;
import com.simplerishta.cms.service.dto.VerifiableAttributesDTO;
import com.simplerishta.cms.service.mapper.VerifiableAttributesMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.simplerishta.cms.domain.VerifiableAttributes}.
 */
@Service
@Transactional
public class VerifiableAttributesService {

    private static final Logger LOG = LoggerFactory.getLogger(VerifiableAttributesService.class);

    private final VerifiableAttributesRepository verifiableAttributesRepository;

    private final VerifiableAttributesMapper verifiableAttributesMapper;

    public VerifiableAttributesService(
        VerifiableAttributesRepository verifiableAttributesRepository,
        VerifiableAttributesMapper verifiableAttributesMapper
    ) {
        this.verifiableAttributesRepository = verifiableAttributesRepository;
        this.verifiableAttributesMapper = verifiableAttributesMapper;
    }

    /**
     * Save a verifiableAttributes.
     *
     * @param verifiableAttributesDTO the entity to save.
     * @return the persisted entity.
     */
    public VerifiableAttributesDTO save(VerifiableAttributesDTO verifiableAttributesDTO) {
        LOG.debug("Request to save VerifiableAttributes : {}", verifiableAttributesDTO);
        VerifiableAttributes verifiableAttributes = verifiableAttributesMapper.toEntity(verifiableAttributesDTO);
        verifiableAttributes = verifiableAttributesRepository.save(verifiableAttributes);
        return verifiableAttributesMapper.toDto(verifiableAttributes);
    }

    /**
     * Update a verifiableAttributes.
     *
     * @param verifiableAttributesDTO the entity to save.
     * @return the persisted entity.
     */
    public VerifiableAttributesDTO update(VerifiableAttributesDTO verifiableAttributesDTO) {
        LOG.debug("Request to update VerifiableAttributes : {}", verifiableAttributesDTO);
        VerifiableAttributes verifiableAttributes = verifiableAttributesMapper.toEntity(verifiableAttributesDTO);
        verifiableAttributes = verifiableAttributesRepository.save(verifiableAttributes);
        return verifiableAttributesMapper.toDto(verifiableAttributes);
    }

    /**
     * Partially update a verifiableAttributes.
     *
     * @param verifiableAttributesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VerifiableAttributesDTO> partialUpdate(VerifiableAttributesDTO verifiableAttributesDTO) {
        LOG.debug("Request to partially update VerifiableAttributes : {}", verifiableAttributesDTO);

        return verifiableAttributesRepository
            .findById(verifiableAttributesDTO.getId())
            .map(existingVerifiableAttributes -> {
                verifiableAttributesMapper.partialUpdate(existingVerifiableAttributes, verifiableAttributesDTO);

                return existingVerifiableAttributes;
            })
            .map(verifiableAttributesRepository::save)
            .map(verifiableAttributesMapper::toDto);
    }

    /**
     * Get all the verifiableAttributes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<VerifiableAttributesDTO> findAll() {
        LOG.debug("Request to get all VerifiableAttributes");
        return verifiableAttributesRepository
            .findAll()
            .stream()
            .map(verifiableAttributesMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one verifiableAttributes by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VerifiableAttributesDTO> findOne(Long id) {
        LOG.debug("Request to get VerifiableAttributes : {}", id);
        return verifiableAttributesRepository.findById(id).map(verifiableAttributesMapper::toDto);
    }

    /**
     * Delete the verifiableAttributes by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete VerifiableAttributes : {}", id);
        verifiableAttributesRepository.deleteById(id);
    }
}
