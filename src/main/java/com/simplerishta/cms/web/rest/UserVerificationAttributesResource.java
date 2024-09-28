package com.simplerishta.cms.web.rest;

import com.simplerishta.cms.repository.UserVerificationAttributesRepository;
import com.simplerishta.cms.service.UserVerificationAttributesService;
import com.simplerishta.cms.service.dto.UserVerificationAttributesDTO;
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
 * REST controller for managing {@link com.simplerishta.cms.domain.UserVerificationAttributes}.
 */
@RestController
@RequestMapping("/api/user-verification-attributes")
public class UserVerificationAttributesResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserVerificationAttributesResource.class);

    private static final String ENTITY_NAME = "simpleRishtaUserVerificationAttributes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserVerificationAttributesService userVerificationAttributesService;

    private final UserVerificationAttributesRepository userVerificationAttributesRepository;

    public UserVerificationAttributesResource(
        UserVerificationAttributesService userVerificationAttributesService,
        UserVerificationAttributesRepository userVerificationAttributesRepository
    ) {
        this.userVerificationAttributesService = userVerificationAttributesService;
        this.userVerificationAttributesRepository = userVerificationAttributesRepository;
    }

    /**
     * {@code POST  /user-verification-attributes} : Create a new userVerificationAttributes.
     *
     * @param userVerificationAttributesDTO the userVerificationAttributesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userVerificationAttributesDTO, or with status {@code 400 (Bad Request)} if the userVerificationAttributes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserVerificationAttributesDTO> createUserVerificationAttributes(
        @Valid @RequestBody UserVerificationAttributesDTO userVerificationAttributesDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save UserVerificationAttributes : {}", userVerificationAttributesDTO);
        if (userVerificationAttributesDTO.getId() != null) {
            throw new BadRequestAlertException("A new userVerificationAttributes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userVerificationAttributesDTO = userVerificationAttributesService.save(userVerificationAttributesDTO);
        return ResponseEntity.created(new URI("/api/user-verification-attributes/" + userVerificationAttributesDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userVerificationAttributesDTO.getId().toString())
            )
            .body(userVerificationAttributesDTO);
    }

    /**
     * {@code PUT  /user-verification-attributes/:id} : Updates an existing userVerificationAttributes.
     *
     * @param id the id of the userVerificationAttributesDTO to save.
     * @param userVerificationAttributesDTO the userVerificationAttributesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userVerificationAttributesDTO,
     * or with status {@code 400 (Bad Request)} if the userVerificationAttributesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userVerificationAttributesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserVerificationAttributesDTO> updateUserVerificationAttributes(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserVerificationAttributesDTO userVerificationAttributesDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserVerificationAttributes : {}, {}", id, userVerificationAttributesDTO);
        if (userVerificationAttributesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userVerificationAttributesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userVerificationAttributesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userVerificationAttributesDTO = userVerificationAttributesService.update(userVerificationAttributesDTO);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userVerificationAttributesDTO.getId().toString())
            )
            .body(userVerificationAttributesDTO);
    }

    /**
     * {@code PATCH  /user-verification-attributes/:id} : Partial updates given fields of an existing userVerificationAttributes, field will ignore if it is null
     *
     * @param id the id of the userVerificationAttributesDTO to save.
     * @param userVerificationAttributesDTO the userVerificationAttributesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userVerificationAttributesDTO,
     * or with status {@code 400 (Bad Request)} if the userVerificationAttributesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userVerificationAttributesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userVerificationAttributesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserVerificationAttributesDTO> partialUpdateUserVerificationAttributes(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserVerificationAttributesDTO userVerificationAttributesDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserVerificationAttributes partially : {}, {}", id, userVerificationAttributesDTO);
        if (userVerificationAttributesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userVerificationAttributesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userVerificationAttributesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserVerificationAttributesDTO> result = userVerificationAttributesService.partialUpdate(userVerificationAttributesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userVerificationAttributesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-verification-attributes} : get all the userVerificationAttributes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userVerificationAttributes in body.
     */
    @GetMapping("")
    public List<UserVerificationAttributesDTO> getAllUserVerificationAttributes() {
        LOG.debug("REST request to get all UserVerificationAttributes");
        return userVerificationAttributesService.findAll();
    }

    /**
     * {@code GET  /user-verification-attributes/:id} : get the "id" userVerificationAttributes.
     *
     * @param id the id of the userVerificationAttributesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userVerificationAttributesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserVerificationAttributesDTO> getUserVerificationAttributes(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserVerificationAttributes : {}", id);
        Optional<UserVerificationAttributesDTO> userVerificationAttributesDTO = userVerificationAttributesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userVerificationAttributesDTO);
    }

    /**
     * {@code DELETE  /user-verification-attributes/:id} : delete the "id" userVerificationAttributes.
     *
     * @param id the id of the userVerificationAttributesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserVerificationAttributes(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserVerificationAttributes : {}", id);
        userVerificationAttributesService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
