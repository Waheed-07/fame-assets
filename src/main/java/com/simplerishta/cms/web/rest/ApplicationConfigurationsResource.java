package com.simplerishta.cms.web.rest;

import com.simplerishta.cms.repository.ApplicationConfigurationsRepository;
import com.simplerishta.cms.service.ApplicationConfigurationsService;
import com.simplerishta.cms.service.dto.ApplicationConfigurationsDTO;
import com.simplerishta.cms.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.simplerishta.cms.domain.ApplicationConfigurations}.
 */
@RestController
@RequestMapping("/api/application-configurations")
public class ApplicationConfigurationsResource {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationConfigurationsResource.class);

    private static final String ENTITY_NAME = "simpleRishtaApplicationConfigurations";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApplicationConfigurationsService applicationConfigurationsService;

    private final ApplicationConfigurationsRepository applicationConfigurationsRepository;

    public ApplicationConfigurationsResource(
        ApplicationConfigurationsService applicationConfigurationsService,
        ApplicationConfigurationsRepository applicationConfigurationsRepository
    ) {
        this.applicationConfigurationsService = applicationConfigurationsService;
        this.applicationConfigurationsRepository = applicationConfigurationsRepository;
    }

    /**
     * {@code POST  /application-configurations} : Create a new applicationConfigurations.
     *
     * @param applicationConfigurationsDTO the applicationConfigurationsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new applicationConfigurationsDTO, or with status {@code 400 (Bad Request)} if the applicationConfigurations has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ApplicationConfigurationsDTO> createApplicationConfigurations(
        @Valid @RequestBody ApplicationConfigurationsDTO applicationConfigurationsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ApplicationConfigurations : {}", applicationConfigurationsDTO);
        if (applicationConfigurationsDTO.getId() != null) {
            throw new BadRequestAlertException("A new applicationConfigurations cannot already have an ID", ENTITY_NAME, "idexists");
        }
        applicationConfigurationsDTO = applicationConfigurationsService.save(applicationConfigurationsDTO);
        return ResponseEntity.created(new URI("/api/application-configurations/" + applicationConfigurationsDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, applicationConfigurationsDTO.getId().toString())
            )
            .body(applicationConfigurationsDTO);
    }

    /**
     * {@code PUT  /application-configurations/:id} : Updates an existing applicationConfigurations.
     *
     * @param id the id of the applicationConfigurationsDTO to save.
     * @param applicationConfigurationsDTO the applicationConfigurationsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationConfigurationsDTO,
     * or with status {@code 400 (Bad Request)} if the applicationConfigurationsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the applicationConfigurationsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApplicationConfigurationsDTO> updateApplicationConfigurations(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ApplicationConfigurationsDTO applicationConfigurationsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ApplicationConfigurations : {}, {}", id, applicationConfigurationsDTO);
        if (applicationConfigurationsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationConfigurationsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationConfigurationsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        applicationConfigurationsDTO = applicationConfigurationsService.update(applicationConfigurationsDTO);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, applicationConfigurationsDTO.getId().toString())
            )
            .body(applicationConfigurationsDTO);
    }

    /**
     * {@code PATCH  /application-configurations/:id} : Partial updates given fields of an existing applicationConfigurations, field will ignore if it is null
     *
     * @param id the id of the applicationConfigurationsDTO to save.
     * @param applicationConfigurationsDTO the applicationConfigurationsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationConfigurationsDTO,
     * or with status {@code 400 (Bad Request)} if the applicationConfigurationsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the applicationConfigurationsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the applicationConfigurationsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ApplicationConfigurationsDTO> partialUpdateApplicationConfigurations(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ApplicationConfigurationsDTO applicationConfigurationsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ApplicationConfigurations partially : {}, {}", id, applicationConfigurationsDTO);
        if (applicationConfigurationsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationConfigurationsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationConfigurationsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApplicationConfigurationsDTO> result = applicationConfigurationsService.partialUpdate(applicationConfigurationsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, applicationConfigurationsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /application-configurations} : get all the applicationConfigurations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of applicationConfigurations in body.
     */
    @GetMapping("")
    public List<ApplicationConfigurationsDTO> getAllApplicationConfigurations() {
        LOG.debug("REST request to get all ApplicationConfigurations");
        return applicationConfigurationsService.findAll();
    }

    /**
     * {@code GET  /application-configurations/:id} : get the "id" applicationConfigurations.
     *
     * @param id the id of the applicationConfigurationsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the applicationConfigurationsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationConfigurationsDTO> getApplicationConfigurations(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ApplicationConfigurations : {}", id);
        Optional<ApplicationConfigurationsDTO> applicationConfigurationsDTO = applicationConfigurationsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(applicationConfigurationsDTO);
    }

    /**
     * {@code DELETE  /application-configurations/:id} : delete the "id" applicationConfigurations.
     *
     * @param id the id of the applicationConfigurationsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplicationConfigurations(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ApplicationConfigurations : {}", id);
        applicationConfigurationsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
