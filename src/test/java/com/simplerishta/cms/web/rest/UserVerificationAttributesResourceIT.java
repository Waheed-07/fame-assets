package com.simplerishta.cms.web.rest;

import static com.simplerishta.cms.domain.UserVerificationAttributesAsserts.*;
import static com.simplerishta.cms.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplerishta.cms.IntegrationTest;
import com.simplerishta.cms.domain.UserVerificationAttributes;
import com.simplerishta.cms.repository.UserVerificationAttributesRepository;
import com.simplerishta.cms.service.dto.UserVerificationAttributesDTO;
import com.simplerishta.cms.service.mapper.UserVerificationAttributesMapper;
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
 * Integration tests for the {@link UserVerificationAttributesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserVerificationAttributesResourceIT {

    private static final String DEFAULT_DOCUMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_URL = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_URL = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_VERIFICATION_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_VERIFICATION_TOKEN = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_ACTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_ACTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/user-verification-attributes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserVerificationAttributesRepository userVerificationAttributesRepository;

    @Autowired
    private UserVerificationAttributesMapper userVerificationAttributesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserVerificationAttributesMockMvc;

    private UserVerificationAttributes userVerificationAttributes;

    private UserVerificationAttributes insertedUserVerificationAttributes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserVerificationAttributes createEntity() {
        return new UserVerificationAttributes()
            .documentType(DEFAULT_DOCUMENT_TYPE)
            .documentUrl(DEFAULT_DOCUMENT_URL)
            .status(DEFAULT_STATUS)
            .verificationToken(DEFAULT_VERIFICATION_TOKEN)
            .lastActionDate(DEFAULT_LAST_ACTION_DATE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserVerificationAttributes createUpdatedEntity() {
        return new UserVerificationAttributes()
            .documentType(UPDATED_DOCUMENT_TYPE)
            .documentUrl(UPDATED_DOCUMENT_URL)
            .status(UPDATED_STATUS)
            .verificationToken(UPDATED_VERIFICATION_TOKEN)
            .lastActionDate(UPDATED_LAST_ACTION_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
    }

    @BeforeEach
    public void initTest() {
        userVerificationAttributes = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserVerificationAttributes != null) {
            userVerificationAttributesRepository.delete(insertedUserVerificationAttributes);
            insertedUserVerificationAttributes = null;
        }
    }

    @Test
    @Transactional
    void createUserVerificationAttributes() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserVerificationAttributes
        UserVerificationAttributesDTO userVerificationAttributesDTO = userVerificationAttributesMapper.toDto(userVerificationAttributes);
        var returnedUserVerificationAttributesDTO = om.readValue(
            restUserVerificationAttributesMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(userVerificationAttributesDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserVerificationAttributesDTO.class
        );

        // Validate the UserVerificationAttributes in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserVerificationAttributes = userVerificationAttributesMapper.toEntity(returnedUserVerificationAttributesDTO);
        assertUserVerificationAttributesUpdatableFieldsEquals(
            returnedUserVerificationAttributes,
            getPersistedUserVerificationAttributes(returnedUserVerificationAttributes)
        );

        insertedUserVerificationAttributes = returnedUserVerificationAttributes;
    }

    @Test
    @Transactional
    void createUserVerificationAttributesWithExistingId() throws Exception {
        // Create the UserVerificationAttributes with an existing ID
        userVerificationAttributes.setId(1L);
        UserVerificationAttributesDTO userVerificationAttributesDTO = userVerificationAttributesMapper.toDto(userVerificationAttributes);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserVerificationAttributesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userVerificationAttributesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserVerificationAttributes in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userVerificationAttributes.setCreatedAt(null);

        // Create the UserVerificationAttributes, which fails.
        UserVerificationAttributesDTO userVerificationAttributesDTO = userVerificationAttributesMapper.toDto(userVerificationAttributes);

        restUserVerificationAttributesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userVerificationAttributesDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserVerificationAttributes() throws Exception {
        // Initialize the database
        insertedUserVerificationAttributes = userVerificationAttributesRepository.saveAndFlush(userVerificationAttributes);

        // Get all the userVerificationAttributesList
        restUserVerificationAttributesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userVerificationAttributes.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE)))
            .andExpect(jsonPath("$.[*].documentUrl").value(hasItem(DEFAULT_DOCUMENT_URL)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].verificationToken").value(hasItem(DEFAULT_VERIFICATION_TOKEN)))
            .andExpect(jsonPath("$.[*].lastActionDate").value(hasItem(DEFAULT_LAST_ACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getUserVerificationAttributes() throws Exception {
        // Initialize the database
        insertedUserVerificationAttributes = userVerificationAttributesRepository.saveAndFlush(userVerificationAttributes);

        // Get the userVerificationAttributes
        restUserVerificationAttributesMockMvc
            .perform(get(ENTITY_API_URL_ID, userVerificationAttributes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userVerificationAttributes.getId().intValue()))
            .andExpect(jsonPath("$.documentType").value(DEFAULT_DOCUMENT_TYPE))
            .andExpect(jsonPath("$.documentUrl").value(DEFAULT_DOCUMENT_URL))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.verificationToken").value(DEFAULT_VERIFICATION_TOKEN))
            .andExpect(jsonPath("$.lastActionDate").value(DEFAULT_LAST_ACTION_DATE.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserVerificationAttributes() throws Exception {
        // Get the userVerificationAttributes
        restUserVerificationAttributesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserVerificationAttributes() throws Exception {
        // Initialize the database
        insertedUserVerificationAttributes = userVerificationAttributesRepository.saveAndFlush(userVerificationAttributes);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userVerificationAttributes
        UserVerificationAttributes updatedUserVerificationAttributes = userVerificationAttributesRepository
            .findById(userVerificationAttributes.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedUserVerificationAttributes are not directly saved in db
        em.detach(updatedUserVerificationAttributes);
        updatedUserVerificationAttributes
            .documentType(UPDATED_DOCUMENT_TYPE)
            .documentUrl(UPDATED_DOCUMENT_URL)
            .status(UPDATED_STATUS)
            .verificationToken(UPDATED_VERIFICATION_TOKEN)
            .lastActionDate(UPDATED_LAST_ACTION_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        UserVerificationAttributesDTO userVerificationAttributesDTO = userVerificationAttributesMapper.toDto(
            updatedUserVerificationAttributes
        );

        restUserVerificationAttributesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userVerificationAttributesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userVerificationAttributesDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserVerificationAttributes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserVerificationAttributesToMatchAllProperties(updatedUserVerificationAttributes);
    }

    @Test
    @Transactional
    void putNonExistingUserVerificationAttributes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userVerificationAttributes.setId(longCount.incrementAndGet());

        // Create the UserVerificationAttributes
        UserVerificationAttributesDTO userVerificationAttributesDTO = userVerificationAttributesMapper.toDto(userVerificationAttributes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserVerificationAttributesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userVerificationAttributesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userVerificationAttributesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserVerificationAttributes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserVerificationAttributes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userVerificationAttributes.setId(longCount.incrementAndGet());

        // Create the UserVerificationAttributes
        UserVerificationAttributesDTO userVerificationAttributesDTO = userVerificationAttributesMapper.toDto(userVerificationAttributes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserVerificationAttributesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userVerificationAttributesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserVerificationAttributes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserVerificationAttributes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userVerificationAttributes.setId(longCount.incrementAndGet());

        // Create the UserVerificationAttributes
        UserVerificationAttributesDTO userVerificationAttributesDTO = userVerificationAttributesMapper.toDto(userVerificationAttributes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserVerificationAttributesMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userVerificationAttributesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserVerificationAttributes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserVerificationAttributesWithPatch() throws Exception {
        // Initialize the database
        insertedUserVerificationAttributes = userVerificationAttributesRepository.saveAndFlush(userVerificationAttributes);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userVerificationAttributes using partial update
        UserVerificationAttributes partialUpdatedUserVerificationAttributes = new UserVerificationAttributes();
        partialUpdatedUserVerificationAttributes.setId(userVerificationAttributes.getId());

        partialUpdatedUserVerificationAttributes
            .documentUrl(UPDATED_DOCUMENT_URL)
            .status(UPDATED_STATUS)
            .lastActionDate(UPDATED_LAST_ACTION_DATE);

        restUserVerificationAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserVerificationAttributes.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserVerificationAttributes))
            )
            .andExpect(status().isOk());

        // Validate the UserVerificationAttributes in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserVerificationAttributesUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserVerificationAttributes, userVerificationAttributes),
            getPersistedUserVerificationAttributes(userVerificationAttributes)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserVerificationAttributesWithPatch() throws Exception {
        // Initialize the database
        insertedUserVerificationAttributes = userVerificationAttributesRepository.saveAndFlush(userVerificationAttributes);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userVerificationAttributes using partial update
        UserVerificationAttributes partialUpdatedUserVerificationAttributes = new UserVerificationAttributes();
        partialUpdatedUserVerificationAttributes.setId(userVerificationAttributes.getId());

        partialUpdatedUserVerificationAttributes
            .documentType(UPDATED_DOCUMENT_TYPE)
            .documentUrl(UPDATED_DOCUMENT_URL)
            .status(UPDATED_STATUS)
            .verificationToken(UPDATED_VERIFICATION_TOKEN)
            .lastActionDate(UPDATED_LAST_ACTION_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restUserVerificationAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserVerificationAttributes.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserVerificationAttributes))
            )
            .andExpect(status().isOk());

        // Validate the UserVerificationAttributes in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserVerificationAttributesUpdatableFieldsEquals(
            partialUpdatedUserVerificationAttributes,
            getPersistedUserVerificationAttributes(partialUpdatedUserVerificationAttributes)
        );
    }

    @Test
    @Transactional
    void patchNonExistingUserVerificationAttributes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userVerificationAttributes.setId(longCount.incrementAndGet());

        // Create the UserVerificationAttributes
        UserVerificationAttributesDTO userVerificationAttributesDTO = userVerificationAttributesMapper.toDto(userVerificationAttributes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserVerificationAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userVerificationAttributesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userVerificationAttributesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserVerificationAttributes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserVerificationAttributes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userVerificationAttributes.setId(longCount.incrementAndGet());

        // Create the UserVerificationAttributes
        UserVerificationAttributesDTO userVerificationAttributesDTO = userVerificationAttributesMapper.toDto(userVerificationAttributes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserVerificationAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userVerificationAttributesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserVerificationAttributes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserVerificationAttributes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userVerificationAttributes.setId(longCount.incrementAndGet());

        // Create the UserVerificationAttributes
        UserVerificationAttributesDTO userVerificationAttributesDTO = userVerificationAttributesMapper.toDto(userVerificationAttributes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserVerificationAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userVerificationAttributesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserVerificationAttributes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserVerificationAttributes() throws Exception {
        // Initialize the database
        insertedUserVerificationAttributes = userVerificationAttributesRepository.saveAndFlush(userVerificationAttributes);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userVerificationAttributes
        restUserVerificationAttributesMockMvc
            .perform(delete(ENTITY_API_URL_ID, userVerificationAttributes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userVerificationAttributesRepository.count();
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

    protected UserVerificationAttributes getPersistedUserVerificationAttributes(UserVerificationAttributes userVerificationAttributes) {
        return userVerificationAttributesRepository.findById(userVerificationAttributes.getId()).orElseThrow();
    }

    protected void assertPersistedUserVerificationAttributesToMatchAllProperties(
        UserVerificationAttributes expectedUserVerificationAttributes
    ) {
        assertUserVerificationAttributesAllPropertiesEquals(
            expectedUserVerificationAttributes,
            getPersistedUserVerificationAttributes(expectedUserVerificationAttributes)
        );
    }

    protected void assertPersistedUserVerificationAttributesToMatchUpdatableProperties(
        UserVerificationAttributes expectedUserVerificationAttributes
    ) {
        assertUserVerificationAttributesAllUpdatablePropertiesEquals(
            expectedUserVerificationAttributes,
            getPersistedUserVerificationAttributes(expectedUserVerificationAttributes)
        );
    }
}
