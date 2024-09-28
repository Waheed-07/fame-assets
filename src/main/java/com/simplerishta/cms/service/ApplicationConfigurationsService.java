package com.simplerishta.cms.service;

import com.simplerishta.cms.domain.ApplicationConfigurations;
import com.simplerishta.cms.repository.ApplicationConfigurationsRepository;
import com.simplerishta.cms.service.dto.ApplicationConfigurationsDTO;
import com.simplerishta.cms.service.mapper.ApplicationConfigurationsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.simplerishta.cms.domain.ApplicationConfigurations}.
 */
@Service
@Transactional
public class ApplicationConfigurationsService {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationConfigurationsService.class);

    private final ApplicationConfigurationsRepository applicationConfigurationsRepository;

    private final ApplicationConfigurationsMapper applicationConfigurationsMapper;

    public ApplicationConfigurationsService(
        ApplicationConfigurationsRepository applicationConfigurationsRepository,
        ApplicationConfigurationsMapper applicationConfigurationsMapper
    ) {
        this.applicationConfigurationsRepository = applicationConfigurationsRepository;
        this.applicationConfigurationsMapper = applicationConfigurationsMapper;
    }

    /**
     * Save a applicationConfigurations.
     *
     * @param applicationConfigurationsDTO the entity to save.
     * @return the persisted entity.
     */
    public ApplicationConfigurationsDTO save(ApplicationConfigurationsDTO applicationConfigurationsDTO) {
        LOG.debug("Request to save ApplicationConfigurations : {}", applicationConfigurationsDTO);
        ApplicationConfigurations applicationConfigurations = applicationConfigurationsMapper.toEntity(applicationConfigurationsDTO);
        applicationConfigurations = applicationConfigurationsRepository.save(applicationConfigurations);
        return applicationConfigurationsMapper.toDto(applicationConfigurations);
    }

    /**
     * Update a applicationConfigurations.
     *
     * @param applicationConfigurationsDTO the entity to save.
     * @return the persisted entity.
     */
    public ApplicationConfigurationsDTO update(ApplicationConfigurationsDTO applicationConfigurationsDTO) {
        LOG.debug("Request to update ApplicationConfigurations : {}", applicationConfigurationsDTO);
        ApplicationConfigurations applicationConfigurations = applicationConfigurationsMapper.toEntity(applicationConfigurationsDTO);
        applicationConfigurations = applicationConfigurationsRepository.save(applicationConfigurations);
        return applicationConfigurationsMapper.toDto(applicationConfigurations);
    }

    /**
     * Partially update a applicationConfigurations.
     *
     * @param applicationConfigurationsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ApplicationConfigurationsDTO> partialUpdate(ApplicationConfigurationsDTO applicationConfigurationsDTO) {
        LOG.debug("Request to partially update ApplicationConfigurations : {}", applicationConfigurationsDTO);

        return applicationConfigurationsRepository
            .findById(applicationConfigurationsDTO.getId())
            .map(existingApplicationConfigurations -> {
                applicationConfigurationsMapper.partialUpdate(existingApplicationConfigurations, applicationConfigurationsDTO);

                return existingApplicationConfigurations;
            })
            .map(applicationConfigurationsRepository::save)
            .map(applicationConfigurationsMapper::toDto);
    }

    /**
     * Get all the applicationConfigurations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ApplicationConfigurationsDTO> findAll() {
        LOG.debug("Request to get all ApplicationConfigurations");
        return applicationConfigurationsRepository
            .findAll()
            .stream()
            .map(applicationConfigurationsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one applicationConfigurations by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ApplicationConfigurationsDTO> findOne(Long id) {
        LOG.debug("Request to get ApplicationConfigurations : {}", id);
        return applicationConfigurationsRepository.findById(id).map(applicationConfigurationsMapper::toDto);
    }

    /**
     * Delete the applicationConfigurations by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ApplicationConfigurations : {}", id);
        applicationConfigurationsRepository.deleteById(id);
    }
}
