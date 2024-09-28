package com.simplerishta.cms.web.rest;

import static com.simplerishta.cms.domain.ApplicationConfigurationsAsserts.*;
import static com.simplerishta.cms.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplerishta.cms.IntegrationTest;
import com.simplerishta.cms.domain.ApplicationConfigurations;
import com.simplerishta.cms.repository.ApplicationConfigurationsRepository;
import com.simplerishta.cms.service.dto.ApplicationConfigurationsDTO;
import com.simplerishta.cms.service.mapper.ApplicationConfigurationsMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link ApplicationConfigurationsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApplicationConfigurationsResourceIT {

    private static final String DEFAULT_CONFIG_KEY = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY_CODE = "AAA";
    private static final String UPDATED_COUNTRY_CODE = "BBB";

    private static final String ENTITY_API_URL = "/api/application-configurations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ApplicationConfigurationsRepository applicationConfigurationsRepository;

    @Autowired
    private ApplicationConfigurationsMapper applicationConfigurationsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicationConfigurationsMockMvc;

    private ApplicationConfigurations applicationConfigurations;

    private ApplicationConfigurations insertedApplicationConfigurations;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationConfigurations createEntity() {
        return new ApplicationConfigurations()
            .configKey(DEFAULT_CONFIG_KEY)
            .configValue(DEFAULT_CONFIG_VALUE)
            .description(DEFAULT_DESCRIPTION)
            .countryCode(DEFAULT_COUNTRY_CODE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationConfigurations createUpdatedEntity() {
        return new ApplicationConfigurations()
            .configKey(UPDATED_CONFIG_KEY)
            .configValue(UPDATED_CONFIG_VALUE)
            .description(UPDATED_DESCRIPTION)
            .countryCode(UPDATED_COUNTRY_CODE);
    }

    @BeforeEach
    public void initTest() {
        applicationConfigurations = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedApplicationConfigurations != null) {
            applicationConfigurationsRepository.delete(insertedApplicationConfigurations);
            insertedApplicationConfigurations = null;
        }
    }

    @Test
    @Transactional
    void createApplicationConfigurations() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ApplicationConfigurations
        ApplicationConfigurationsDTO applicationConfigurationsDTO = applicationConfigurationsMapper.toDto(applicationConfigurations);
        var returnedApplicationConfigurationsDTO = om.readValue(
            restApplicationConfigurationsMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationConfigurationsDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ApplicationConfigurationsDTO.class
        );

        // Validate the ApplicationConfigurations in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedApplicationConfigurations = applicationConfigurationsMapper.toEntity(returnedApplicationConfigurationsDTO);
        assertApplicationConfigurationsUpdatableFieldsEquals(
            returnedApplicationConfigurations,
            getPersistedApplicationConfigurations(returnedApplicationConfigurations)
        );

        insertedApplicationConfigurations = returnedApplicationConfigurations;
    }

    @Test
    @Transactional
    void createApplicationConfigurationsWithExistingId() throws Exception {
        // Create the ApplicationConfigurations with an existing ID
        applicationConfigurations.setId(1L);
        ApplicationConfigurationsDTO applicationConfigurationsDTO = applicationConfigurationsMapper.toDto(applicationConfigurations);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationConfigurationsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationConfigurationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationConfigurations in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkConfigKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        applicationConfigurations.setConfigKey(null);

        // Create the ApplicationConfigurations, which fails.
        ApplicationConfigurationsDTO applicationConfigurationsDTO = applicationConfigurationsMapper.toDto(applicationConfigurations);

        restApplicationConfigurationsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationConfigurationsDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConfigValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        applicationConfigurations.setConfigValue(null);

        // Create the ApplicationConfigurations, which fails.
        ApplicationConfigurationsDTO applicationConfigurationsDTO = applicationConfigurationsMapper.toDto(applicationConfigurations);

        restApplicationConfigurationsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationConfigurationsDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApplicationConfigurations() throws Exception {
        // Initialize the database
        insertedApplicationConfigurations = applicationConfigurationsRepository.saveAndFlush(applicationConfigurations);

        // Get all the applicationConfigurationsList
        restApplicationConfigurationsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationConfigurations.getId().intValue())))
            .andExpect(jsonPath("$.[*].configKey").value(hasItem(DEFAULT_CONFIG_KEY)))
            .andExpect(jsonPath("$.[*].configValue").value(hasItem(DEFAULT_CONFIG_VALUE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE)));
    }

    @Test
    @Transactional
    void getApplicationConfigurations() throws Exception {
        // Initialize the database
        insertedApplicationConfigurations = applicationConfigurationsRepository.saveAndFlush(applicationConfigurations);

        // Get the applicationConfigurations
        restApplicationConfigurationsMockMvc
            .perform(get(ENTITY_API_URL_ID, applicationConfigurations.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(applicationConfigurations.getId().intValue()))
            .andExpect(jsonPath("$.configKey").value(DEFAULT_CONFIG_KEY))
            .andExpect(jsonPath("$.configValue").value(DEFAULT_CONFIG_VALUE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.countryCode").value(DEFAULT_COUNTRY_CODE));
    }

    @Test
    @Transactional
    void getNonExistingApplicationConfigurations() throws Exception {
        // Get the applicationConfigurations
        restApplicationConfigurationsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingApplicationConfigurations() throws Exception {
        // Initialize the database
        insertedApplicationConfigurations = applicationConfigurationsRepository.saveAndFlush(applicationConfigurations);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the applicationConfigurations
        ApplicationConfigurations updatedApplicationConfigurations = applicationConfigurationsRepository
            .findById(applicationConfigurations.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedApplicationConfigurations are not directly saved in db
        em.detach(updatedApplicationConfigurations);
        updatedApplicationConfigurations
            .configKey(UPDATED_CONFIG_KEY)
            .configValue(UPDATED_CONFIG_VALUE)
            .description(UPDATED_DESCRIPTION)
            .countryCode(UPDATED_COUNTRY_CODE);
        ApplicationConfigurationsDTO applicationConfigurationsDTO = applicationConfigurationsMapper.toDto(updatedApplicationConfigurations);

        restApplicationConfigurationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicationConfigurationsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(applicationConfigurationsDTO))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationConfigurations in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedApplicationConfigurationsToMatchAllProperties(updatedApplicationConfigurations);
    }

    @Test
    @Transactional
    void putNonExistingApplicationConfigurations() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        applicationConfigurations.setId(longCount.incrementAndGet());

        // Create the ApplicationConfigurations
        ApplicationConfigurationsDTO applicationConfigurationsDTO = applicationConfigurationsMapper.toDto(applicationConfigurations);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationConfigurationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicationConfigurationsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(applicationConfigurationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationConfigurations in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApplicationConfigurations() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        applicationConfigurations.setId(longCount.incrementAndGet());

        // Create the ApplicationConfigurations
        ApplicationConfigurationsDTO applicationConfigurationsDTO = applicationConfigurationsMapper.toDto(applicationConfigurations);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationConfigurationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(applicationConfigurationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationConfigurations in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApplicationConfigurations() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        applicationConfigurations.setId(longCount.incrementAndGet());

        // Create the ApplicationConfigurations
        ApplicationConfigurationsDTO applicationConfigurationsDTO = applicationConfigurationsMapper.toDto(applicationConfigurations);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationConfigurationsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationConfigurationsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationConfigurations in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApplicationConfigurationsWithPatch() throws Exception {
        // Initialize the database
        insertedApplicationConfigurations = applicationConfigurationsRepository.saveAndFlush(applicationConfigurations);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the applicationConfigurations using partial update
        ApplicationConfigurations partialUpdatedApplicationConfigurations = new ApplicationConfigurations();
        partialUpdatedApplicationConfigurations.setId(applicationConfigurations.getId());

        partialUpdatedApplicationConfigurations.description(UPDATED_DESCRIPTION);

        restApplicationConfigurationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationConfigurations.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedApplicationConfigurations))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationConfigurations in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertApplicationConfigurationsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedApplicationConfigurations, applicationConfigurations),
            getPersistedApplicationConfigurations(applicationConfigurations)
        );
    }

    @Test
    @Transactional
    void fullUpdateApplicationConfigurationsWithPatch() throws Exception {
        // Initialize the database
        insertedApplicationConfigurations = applicationConfigurationsRepository.saveAndFlush(applicationConfigurations);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the applicationConfigurations using partial update
        ApplicationConfigurations partialUpdatedApplicationConfigurations = new ApplicationConfigurations();
        partialUpdatedApplicationConfigurations.setId(applicationConfigurations.getId());

        partialUpdatedApplicationConfigurations
            .configKey(UPDATED_CONFIG_KEY)
            .configValue(UPDATED_CONFIG_VALUE)
            .description(UPDATED_DESCRIPTION)
            .countryCode(UPDATED_COUNTRY_CODE);

        restApplicationConfigurationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationConfigurations.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedApplicationConfigurations))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationConfigurations in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertApplicationConfigurationsUpdatableFieldsEquals(
            partialUpdatedApplicationConfigurations,
            getPersistedApplicationConfigurations(partialUpdatedApplicationConfigurations)
        );
    }

    @Test
    @Transactional
    void patchNonExistingApplicationConfigurations() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        applicationConfigurations.setId(longCount.incrementAndGet());

        // Create the ApplicationConfigurations
        ApplicationConfigurationsDTO applicationConfigurationsDTO = applicationConfigurationsMapper.toDto(applicationConfigurations);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationConfigurationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, applicationConfigurationsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(applicationConfigurationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationConfigurations in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApplicationConfigurations() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        applicationConfigurations.setId(longCount.incrementAndGet());

        // Create the ApplicationConfigurations
        ApplicationConfigurationsDTO applicationConfigurationsDTO = applicationConfigurationsMapper.toDto(applicationConfigurations);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationConfigurationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(applicationConfigurationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationConfigurations in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApplicationConfigurations() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        applicationConfigurations.setId(longCount.incrementAndGet());

        // Create the ApplicationConfigurations
        ApplicationConfigurationsDTO applicationConfigurationsDTO = applicationConfigurationsMapper.toDto(applicationConfigurations);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationConfigurationsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(applicationConfigurationsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationConfigurations in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApplicationConfigurations() throws Exception {
        // Initialize the database
        insertedApplicationConfigurations = applicationConfigurationsRepository.saveAndFlush(applicationConfigurations);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the applicationConfigurations
        restApplicationConfigurationsMockMvc
            .perform(delete(ENTITY_API_URL_ID, applicationConfigurations.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return applicationConfigurationsRepository.count();
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

    protected ApplicationConfigurations getPersistedApplicationConfigurations(ApplicationConfigurations applicationConfigurations) {
        return applicationConfigurationsRepository.findById(applicationConfigurations.getId()).orElseThrow();
    }

    protected void assertPersistedApplicationConfigurationsToMatchAllProperties(
        ApplicationConfigurations expectedApplicationConfigurations
    ) {
        assertApplicationConfigurationsAllPropertiesEquals(
            expectedApplicationConfigurations,
            getPersistedApplicationConfigurations(expectedApplicationConfigurations)
        );
    }

    protected void assertPersistedApplicationConfigurationsToMatchUpdatableProperties(
        ApplicationConfigurations expectedApplicationConfigurations
    ) {
        assertApplicationConfigurationsAllUpdatablePropertiesEquals(
            expectedApplicationConfigurations,
            getPersistedApplicationConfigurations(expectedApplicationConfigurations)
        );
    }
}
