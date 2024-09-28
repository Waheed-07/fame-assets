package com.simplerishta.cms.web.rest;

import static com.simplerishta.cms.domain.VerifiableAttributesAsserts.*;
import static com.simplerishta.cms.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplerishta.cms.IntegrationTest;
import com.simplerishta.cms.domain.VerifiableAttributes;
import com.simplerishta.cms.repository.VerifiableAttributesRepository;
import com.simplerishta.cms.service.dto.VerifiableAttributesDTO;
import com.simplerishta.cms.service.mapper.VerifiableAttributesMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VerifiableAttributesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VerifiableAttributesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/verifiable-attributes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VerifiableAttributesRepository verifiableAttributesRepository;

    @Autowired
    private VerifiableAttributesMapper verifiableAttributesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVerifiableAttributesMockMvc;

    private VerifiableAttributes verifiableAttributes;

    private VerifiableAttributes insertedVerifiableAttributes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VerifiableAttributes createEntity() {
        return new VerifiableAttributes()
            .name(DEFAULT_NAME)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VerifiableAttributes createUpdatedEntity() {
        return new VerifiableAttributes()
            .name(UPDATED_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
    }

    @BeforeEach
    public void initTest() {
        verifiableAttributes = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedVerifiableAttributes != null) {
            verifiableAttributesRepository.delete(insertedVerifiableAttributes);
            insertedVerifiableAttributes = null;
        }
    }

    @Test
    @Transactional
    void createVerifiableAttributes() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the VerifiableAttributes
        VerifiableAttributesDTO verifiableAttributesDTO = verifiableAttributesMapper.toDto(verifiableAttributes);
        var returnedVerifiableAttributesDTO = om.readValue(
            restVerifiableAttributesMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(verifiableAttributesDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VerifiableAttributesDTO.class
        );

        // Validate the VerifiableAttributes in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVerifiableAttributes = verifiableAttributesMapper.toEntity(returnedVerifiableAttributesDTO);
        assertVerifiableAttributesUpdatableFieldsEquals(
            returnedVerifiableAttributes,
            getPersistedVerifiableAttributes(returnedVerifiableAttributes)
        );

        insertedVerifiableAttributes = returnedVerifiableAttributes;
    }

    @Test
    @Transactional
    void createVerifiableAttributesWithExistingId() throws Exception {
        // Create the VerifiableAttributes with an existing ID
        verifiableAttributes.setId(1L);
        VerifiableAttributesDTO verifiableAttributesDTO = verifiableAttributesMapper.toDto(verifiableAttributes);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVerifiableAttributesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(verifiableAttributesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VerifiableAttributes in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        verifiableAttributes.setName(null);

        // Create the VerifiableAttributes, which fails.
        VerifiableAttributesDTO verifiableAttributesDTO = verifiableAttributesMapper.toDto(verifiableAttributes);

        restVerifiableAttributesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(verifiableAttributesDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        verifiableAttributes.setCreatedBy(null);

        // Create the VerifiableAttributes, which fails.
        VerifiableAttributesDTO verifiableAttributesDTO = verifiableAttributesMapper.toDto(verifiableAttributes);

        restVerifiableAttributesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(verifiableAttributesDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        verifiableAttributes.setCreatedAt(null);

        // Create the VerifiableAttributes, which fails.
        VerifiableAttributesDTO verifiableAttributesDTO = verifiableAttributesMapper.toDto(verifiableAttributes);

        restVerifiableAttributesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(verifiableAttributesDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVerifiableAttributes() throws Exception {
        // Initialize the database
        insertedVerifiableAttributes = verifiableAttributesRepository.saveAndFlush(verifiableAttributes);

        // Get all the verifiableAttributesList
        restVerifiableAttributesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(verifiableAttributes.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getVerifiableAttributes() throws Exception {
        // Initialize the database
        insertedVerifiableAttributes = verifiableAttributesRepository.saveAndFlush(verifiableAttributes);

        // Get the verifiableAttributes
        restVerifiableAttributesMockMvc
            .perform(get(ENTITY_API_URL_ID, verifiableAttributes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(verifiableAttributes.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVerifiableAttributes() throws Exception {
        // Get the verifiableAttributes
        restVerifiableAttributesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVerifiableAttributes() throws Exception {
        // Initialize the database
        insertedVerifiableAttributes = verifiableAttributesRepository.saveAndFlush(verifiableAttributes);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the verifiableAttributes
        VerifiableAttributes updatedVerifiableAttributes = verifiableAttributesRepository
            .findById(verifiableAttributes.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedVerifiableAttributes are not directly saved in db
        em.detach(updatedVerifiableAttributes);
        updatedVerifiableAttributes
            .name(UPDATED_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        VerifiableAttributesDTO verifiableAttributesDTO = verifiableAttributesMapper.toDto(updatedVerifiableAttributes);

        restVerifiableAttributesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, verifiableAttributesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(verifiableAttributesDTO))
            )
            .andExpect(status().isOk());

        // Validate the VerifiableAttributes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVerifiableAttributesToMatchAllProperties(updatedVerifiableAttributes);
    }

    @Test
    @Transactional
    void putNonExistingVerifiableAttributes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        verifiableAttributes.setId(longCount.incrementAndGet());

        // Create the VerifiableAttributes
        VerifiableAttributesDTO verifiableAttributesDTO = verifiableAttributesMapper.toDto(verifiableAttributes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVerifiableAttributesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, verifiableAttributesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(verifiableAttributesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VerifiableAttributes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVerifiableAttributes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        verifiableAttributes.setId(longCount.incrementAndGet());

        // Create the VerifiableAttributes
        VerifiableAttributesDTO verifiableAttributesDTO = verifiableAttributesMapper.toDto(verifiableAttributes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVerifiableAttributesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(verifiableAttributesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VerifiableAttributes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVerifiableAttributes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        verifiableAttributes.setId(longCount.incrementAndGet());

        // Create the VerifiableAttributes
        VerifiableAttributesDTO verifiableAttributesDTO = verifiableAttributesMapper.toDto(verifiableAttributes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVerifiableAttributesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(verifiableAttributesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VerifiableAttributes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVerifiableAttributesWithPatch() throws Exception {
        // Initialize the database
        insertedVerifiableAttributes = verifiableAttributesRepository.saveAndFlush(verifiableAttributes);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the verifiableAttributes using partial update
        VerifiableAttributes partialUpdatedVerifiableAttributes = new VerifiableAttributes();
        partialUpdatedVerifiableAttributes.setId(verifiableAttributes.getId());

        partialUpdatedVerifiableAttributes.name(UPDATED_NAME);

        restVerifiableAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVerifiableAttributes.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVerifiableAttributes))
            )
            .andExpect(status().isOk());

        // Validate the VerifiableAttributes in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVerifiableAttributesUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVerifiableAttributes, verifiableAttributes),
            getPersistedVerifiableAttributes(verifiableAttributes)
        );
    }

    @Test
    @Transactional
    void fullUpdateVerifiableAttributesWithPatch() throws Exception {
        // Initialize the database
        insertedVerifiableAttributes = verifiableAttributesRepository.saveAndFlush(verifiableAttributes);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the verifiableAttributes using partial update
        VerifiableAttributes partialUpdatedVerifiableAttributes = new VerifiableAttributes();
        partialUpdatedVerifiableAttributes.setId(verifiableAttributes.getId());

        partialUpdatedVerifiableAttributes
            .name(UPDATED_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restVerifiableAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVerifiableAttributes.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVerifiableAttributes))
            )
            .andExpect(status().isOk());

        // Validate the VerifiableAttributes in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVerifiableAttributesUpdatableFieldsEquals(
            partialUpdatedVerifiableAttributes,
            getPersistedVerifiableAttributes(partialUpdatedVerifiableAttributes)
        );
    }

    @Test
    @Transactional
    void patchNonExistingVerifiableAttributes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        verifiableAttributes.setId(longCount.incrementAndGet());

        // Create the VerifiableAttributes
        VerifiableAttributesDTO verifiableAttributesDTO = verifiableAttributesMapper.toDto(verifiableAttributes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVerifiableAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, verifiableAttributesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(verifiableAttributesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VerifiableAttributes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVerifiableAttributes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        verifiableAttributes.setId(longCount.incrementAndGet());

        // Create the VerifiableAttributes
        VerifiableAttributesDTO verifiableAttributesDTO = verifiableAttributesMapper.toDto(verifiableAttributes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVerifiableAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(verifiableAttributesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VerifiableAttributes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVerifiableAttributes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        verifiableAttributes.setId(longCount.incrementAndGet());

        // Create the VerifiableAttributes
        VerifiableAttributesDTO verifiableAttributesDTO = verifiableAttributesMapper.toDto(verifiableAttributes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVerifiableAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(verifiableAttributesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VerifiableAttributes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVerifiableAttributes() throws Exception {
        // Initialize the database
        insertedVerifiableAttributes = verifiableAttributesRepository.saveAndFlush(verifiableAttributes);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the verifiableAttributes
        restVerifiableAttributesMockMvc
            .perform(delete(ENTITY_API_URL_ID, verifiableAttributes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return verifiableAttributesRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected VerifiableAttributes getPersistedVerifiableAttributes(VerifiableAttributes verifiableAttributes) {
        return verifiableAttributesRepository.findById(verifiableAttributes.getId()).orElseThrow();
    }

    protected void assertPersistedVerifiableAttributesToMatchAllProperties(VerifiableAttributes expectedVerifiableAttributes) {
        assertVerifiableAttributesAllPropertiesEquals(
            expectedVerifiableAttributes,
            getPersistedVerifiableAttributes(expectedVerifiableAttributes)
        );
    }

    protected void assertPersistedVerifiableAttributesToMatchUpdatableProperties(VerifiableAttributes expectedVerifiableAttributes) {
        assertVerifiableAttributesAllUpdatablePropertiesEquals(
            expectedVerifiableAttributes,
            getPersistedVerifiableAttributes(expectedVerifiableAttributes)
        );
    }
}
