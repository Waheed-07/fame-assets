package com.simplerishta.cms.web.rest;

import com.simplerishta.cms.repository.VerifiableAttributesRepository;
import com.simplerishta.cms.service.VerifiableAttributesService;
import com.simplerishta.cms.service.dto.VerifiableAttributesDTO;
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
 * REST controller for managing {@link com.simplerishta.cms.domain.VerifiableAttributes}.
 */
@RestController
@RequestMapping("/api/verifiable-attributes")
public class VerifiableAttributesResource {

    private static final Logger LOG = LoggerFactory.getLogger(VerifiableAttributesResource.class);

    private static final String ENTITY_NAME = "simpleRishtaVerifiableAttributes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VerifiableAttributesService verifiableAttributesService;

    private final VerifiableAttributesRepository verifiableAttributesRepository;

    public VerifiableAttributesResource(
        VerifiableAttributesService verifiableAttributesService,
        VerifiableAttributesRepository verifiableAttributesRepository
    ) {
        this.verifiableAttributesService = verifiableAttributesService;
        this.verifiableAttributesRepository = verifiableAttributesRepository;
    }

    /**
     * {@code POST  /verifiable-attributes} : Create a new verifiableAttributes.
     *
     * @param verifiableAttributesDTO the verifiableAttributesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new verifiableAttributesDTO, or with status {@code 400 (Bad Request)} if the verifiableAttributes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VerifiableAttributesDTO> createVerifiableAttributes(
        @Valid @RequestBody VerifiableAttributesDTO verifiableAttributesDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save VerifiableAttributes : {}", verifiableAttributesDTO);
        if (verifiableAttributesDTO.getId() != null) {
            throw new BadRequestAlertException("A new verifiableAttributes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        verifiableAttributesDTO = verifiableAttributesService.save(verifiableAttributesDTO);
        return ResponseEntity.created(new URI("/api/verifiable-attributes/" + verifiableAttributesDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, verifiableAttributesDTO.getId().toString()))
            .body(verifiableAttributesDTO);
    }

    /**
     * {@code PUT  /verifiable-attributes/:id} : Updates an existing verifiableAttributes.
     *
     * @param id the id of the verifiableAttributesDTO to save.
     * @param verifiableAttributesDTO the verifiableAttributesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated verifiableAttributesDTO,
     * or with status {@code 400 (Bad Request)} if the verifiableAttributesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the verifiableAttributesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VerifiableAttributesDTO> updateVerifiableAttributes(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VerifiableAttributesDTO verifiableAttributesDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update VerifiableAttributes : {}, {}", id, verifiableAttributesDTO);
        if (verifiableAttributesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, verifiableAttributesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!verifiableAttributesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        verifiableAttributesDTO = verifiableAttributesService.update(verifiableAttributesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, verifiableAttributesDTO.getId().toString()))
            .body(verifiableAttributesDTO);
    }

    /**
     * {@code PATCH  /verifiable-attributes/:id} : Partial updates given fields of an existing verifiableAttributes, field will ignore if it is null
     *
     * @param id the id of the verifiableAttributesDTO to save.
     * @param verifiableAttributesDTO the verifiableAttributesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated verifiableAttributesDTO,
     * or with status {@code 400 (Bad Request)} if the verifiableAttributesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the verifiableAttributesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the verifiableAttributesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VerifiableAttributesDTO> partialUpdateVerifiableAttributes(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VerifiableAttributesDTO verifiableAttributesDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update VerifiableAttributes partially : {}, {}", id, verifiableAttributesDTO);
        if (verifiableAttributesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, verifiableAttributesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!verifiableAttributesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VerifiableAttributesDTO> result = verifiableAttributesService.partialUpdate(verifiableAttributesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, verifiableAttributesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /verifiable-attributes} : get all the verifiableAttributes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of verifiableAttributes in body.
     */
    @GetMapping("")
    public List<VerifiableAttributesDTO> getAllVerifiableAttributes() {
        LOG.debug("REST request to get all VerifiableAttributes");
        return verifiableAttributesService.findAll();
    }

    /**
     * {@code GET  /verifiable-attributes/:id} : get the "id" verifiableAttributes.
     *
     * @param id the id of the verifiableAttributesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the verifiableAttributesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VerifiableAttributesDTO> getVerifiableAttributes(@PathVariable("id") Long id) {
        LOG.debug("REST request to get VerifiableAttributes : {}", id);
        Optional<VerifiableAttributesDTO> verifiableAttributesDTO = verifiableAttributesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(verifiableAttributesDTO);
    }

    /**
     * {@code DELETE  /verifiable-attributes/:id} : delete the "id" verifiableAttributes.
     *
     * @param id the id of the verifiableAttributesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVerifiableAttributes(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete VerifiableAttributes : {}", id);
        verifiableAttributesService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
