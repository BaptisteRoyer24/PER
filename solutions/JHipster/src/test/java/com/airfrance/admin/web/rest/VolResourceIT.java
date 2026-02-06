package com.airfrance.admin.web.rest;

import static com.airfrance.admin.domain.VolAsserts.*;
import static com.airfrance.admin.web.rest.TestUtil.createUpdateProxyForBean;
import static com.airfrance.admin.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.airfrance.admin.IntegrationTest;
import com.airfrance.admin.domain.Vol;
import com.airfrance.admin.repository.VolRepository;
import com.airfrance.admin.service.dto.VolDTO;
import com.airfrance.admin.service.mapper.VolMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link VolResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VolResourceIT {

    private static final String DEFAULT_ORIGIN = "AAA";
    private static final String UPDATED_ORIGIN = "BBB";

    private static final String DEFAULT_DESTINATION = "AAA";
    private static final String UPDATED_DESTINATION = "BBB";

    private static final Boolean DEFAULT_ALLER_RETOUR = false;
    private static final Boolean UPDATED_ALLER_RETOUR = true;

    private static final BigDecimal DEFAULT_PRIX = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRIX = new BigDecimal(1);
    private static final BigDecimal SMALLER_PRIX = new BigDecimal(0 - 1);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/vols";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VolRepository volRepository;

    @Autowired
    private VolMapper volMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVolMockMvc;

    private Vol vol;

    private Vol insertedVol;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vol createEntity() {
        return new Vol()
            .origin(DEFAULT_ORIGIN)
            .destination(DEFAULT_DESTINATION)
            .allerRetour(DEFAULT_ALLER_RETOUR)
            .prix(DEFAULT_PRIX)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vol createUpdatedEntity() {
        return new Vol()
            .origin(UPDATED_ORIGIN)
            .destination(UPDATED_DESTINATION)
            .allerRetour(UPDATED_ALLER_RETOUR)
            .prix(UPDATED_PRIX)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
    }

    @BeforeEach
    void initTest() {
        vol = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedVol != null) {
            volRepository.delete(insertedVol);
            insertedVol = null;
        }
    }

    @Test
    @Transactional
    void createVol() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Vol
        VolDTO volDTO = volMapper.toDto(vol);
        var returnedVolDTO = om.readValue(
            restVolMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(volDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VolDTO.class
        );

        // Validate the Vol in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVol = volMapper.toEntity(returnedVolDTO);
        assertVolUpdatableFieldsEquals(returnedVol, getPersistedVol(returnedVol));

        insertedVol = returnedVol;
    }

    @Test
    @Transactional
    void createVolWithExistingId() throws Exception {
        // Create the Vol with an existing ID
        vol.setId(1L);
        VolDTO volDTO = volMapper.toDto(vol);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(volDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vol in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOriginIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vol.setOrigin(null);

        // Create the Vol, which fails.
        VolDTO volDTO = volMapper.toDto(vol);

        restVolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(volDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDestinationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vol.setDestination(null);

        // Create the Vol, which fails.
        VolDTO volDTO = volMapper.toDto(vol);

        restVolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(volDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAllerRetourIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vol.setAllerRetour(null);

        // Create the Vol, which fails.
        VolDTO volDTO = volMapper.toDto(vol);

        restVolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(volDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrixIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vol.setPrix(null);

        // Create the Vol, which fails.
        VolDTO volDTO = volMapper.toDto(vol);

        restVolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(volDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVols() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        // Get all the volList
        restVolMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vol.getId().intValue())))
            .andExpect(jsonPath("$.[*].origin").value(hasItem(DEFAULT_ORIGIN)))
            .andExpect(jsonPath("$.[*].destination").value(hasItem(DEFAULT_DESTINATION)))
            .andExpect(jsonPath("$.[*].allerRetour").value(hasItem(DEFAULT_ALLER_RETOUR)))
            .andExpect(jsonPath("$.[*].prix").value(hasItem(sameNumber(DEFAULT_PRIX))))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getVol() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        // Get the vol
        restVolMockMvc
            .perform(get(ENTITY_API_URL_ID, vol.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vol.getId().intValue()))
            .andExpect(jsonPath("$.origin").value(DEFAULT_ORIGIN))
            .andExpect(jsonPath("$.destination").value(DEFAULT_DESTINATION))
            .andExpect(jsonPath("$.allerRetour").value(DEFAULT_ALLER_RETOUR))
            .andExpect(jsonPath("$.prix").value(sameNumber(DEFAULT_PRIX)))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getVolsByIdFiltering() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        Long id = vol.getId();

        defaultVolFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultVolFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultVolFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVolsByOriginIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        // Get all the volList where origin equals to
        defaultVolFiltering("origin.equals=" + DEFAULT_ORIGIN, "origin.equals=" + UPDATED_ORIGIN);
    }

    @Test
    @Transactional
    void getAllVolsByOriginIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        // Get all the volList where origin in
        defaultVolFiltering("origin.in=" + DEFAULT_ORIGIN + "," + UPDATED_ORIGIN, "origin.in=" + UPDATED_ORIGIN);
    }

    @Test
    @Transactional
    void getAllVolsByOriginIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        // Get all the volList where origin is not null
        defaultVolFiltering("origin.specified=true", "origin.specified=false");
    }

    @Test
    @Transactional
    void getAllVolsByOriginContainsSomething() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        // Get all the volList where origin contains
        defaultVolFiltering("origin.contains=" + DEFAULT_ORIGIN, "origin.contains=" + UPDATED_ORIGIN);
    }

    @Test
    @Transactional
    void getAllVolsByOriginNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        // Get all the volList where origin does not contain
        defaultVolFiltering("origin.doesNotContain=" + UPDATED_ORIGIN, "origin.doesNotContain=" + DEFAULT_ORIGIN);
    }

    @Test
    @Transactional
    void getAllVolsByDestinationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        // Get all the volList where destination equals to
        defaultVolFiltering("destination.equals=" + DEFAULT_DESTINATION, "destination.equals=" + UPDATED_DESTINATION);
    }

    @Test
    @Transactional
    void getAllVolsByDestinationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        // Get all the volList where destination in
        defaultVolFiltering("destination.in=" + DEFAULT_DESTINATION + "," + UPDATED_DESTINATION, "destination.in=" + UPDATED_DESTINATION);
    }

    @Test
    @Transactional
    void getAllVolsByDestinationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        // Get all the volList where destination is not null
        defaultVolFiltering("destination.specified=true", "destination.specified=false");
    }

    @Test
    @Transactional
    void getAllVolsByDestinationContainsSomething() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        // Get all the volList where destination contains
        defaultVolFiltering("destination.contains=" + DEFAULT_DESTINATION, "destination.contains=" + UPDATED_DESTINATION);
    }

    @Test
    @Transactional
    void getAllVolsByDestinationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        // Get all the volList where destination does not contain
        defaultVolFiltering("destination.doesNotContain=" + UPDATED_DESTINATION, "destination.doesNotContain=" + DEFAULT_DESTINATION);
    }

    @Test
    @Transactional
    void getAllVolsByAllerRetourIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        // Get all the volList where allerRetour equals to
        defaultVolFiltering("allerRetour.equals=" + DEFAULT_ALLER_RETOUR, "allerRetour.equals=" + UPDATED_ALLER_RETOUR);
    }

    @Test
    @Transactional
    void getAllVolsByAllerRetourIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        // Get all the volList where allerRetour in
        defaultVolFiltering(
            "allerRetour.in=" + DEFAULT_ALLER_RETOUR + "," + UPDATED_ALLER_RETOUR,
            "allerRetour.in=" + UPDATED_ALLER_RETOUR
        );
    }

    @Test
    @Transactional
    void getAllVolsByAllerRetourIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        // Get all the volList where allerRetour is not null
        defaultVolFiltering("allerRetour.specified=true", "allerRetour.specified=false");
    }

    @Test
    @Transactional
    void getAllVolsByPrixIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        // Get all the volList where prix equals to
        defaultVolFiltering("prix.equals=" + DEFAULT_PRIX, "prix.equals=" + UPDATED_PRIX);
    }

    @Test
    @Transactional
    void getAllVolsByPrixIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        // Get all the volList where prix in
        defaultVolFiltering("prix.in=" + DEFAULT_PRIX + "," + UPDATED_PRIX, "prix.in=" + UPDATED_PRIX);
    }

    @Test
    @Transactional
    void getAllVolsByPrixIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        // Get all the volList where prix is not null
        defaultVolFiltering("prix.specified=true", "prix.specified=false");
    }

    @Test
    @Transactional
    void getAllVolsByPrixIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        // Get all the volList where prix is greater than or equal to
        defaultVolFiltering("prix.greaterThanOrEqual=" + DEFAULT_PRIX, "prix.greaterThanOrEqual=" + UPDATED_PRIX);
    }

    @Test
    @Transactional
    void getAllVolsByPrixIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        // Get all the volList where prix is less than or equal to
        defaultVolFiltering("prix.lessThanOrEqual=" + DEFAULT_PRIX, "prix.lessThanOrEqual=" + SMALLER_PRIX);
    }

    @Test
    @Transactional
    void getAllVolsByPrixIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        // Get all the volList where prix is less than
        defaultVolFiltering("prix.lessThan=" + UPDATED_PRIX, "prix.lessThan=" + DEFAULT_PRIX);
    }

    @Test
    @Transactional
    void getAllVolsByPrixIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        // Get all the volList where prix is greater than
        defaultVolFiltering("prix.greaterThan=" + SMALLER_PRIX, "prix.greaterThan=" + DEFAULT_PRIX);
    }

    @Test
    @Transactional
    void getAllVolsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        // Get all the volList where createdAt equals to
        defaultVolFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllVolsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        // Get all the volList where createdAt in
        defaultVolFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllVolsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        // Get all the volList where createdAt is not null
        defaultVolFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllVolsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        // Get all the volList where updatedAt equals to
        defaultVolFiltering("updatedAt.equals=" + DEFAULT_UPDATED_AT, "updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllVolsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        // Get all the volList where updatedAt in
        defaultVolFiltering("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT, "updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllVolsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        // Get all the volList where updatedAt is not null
        defaultVolFiltering("updatedAt.specified=true", "updatedAt.specified=false");
    }

    private void defaultVolFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultVolShouldBeFound(shouldBeFound);
        defaultVolShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVolShouldBeFound(String filter) throws Exception {
        restVolMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vol.getId().intValue())))
            .andExpect(jsonPath("$.[*].origin").value(hasItem(DEFAULT_ORIGIN)))
            .andExpect(jsonPath("$.[*].destination").value(hasItem(DEFAULT_DESTINATION)))
            .andExpect(jsonPath("$.[*].allerRetour").value(hasItem(DEFAULT_ALLER_RETOUR)))
            .andExpect(jsonPath("$.[*].prix").value(hasItem(sameNumber(DEFAULT_PRIX))))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restVolMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVolShouldNotBeFound(String filter) throws Exception {
        restVolMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVolMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVol() throws Exception {
        // Get the vol
        restVolMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVol() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vol
        Vol updatedVol = volRepository.findById(vol.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVol are not directly saved in db
        em.detach(updatedVol);
        updatedVol
            .origin(UPDATED_ORIGIN)
            .destination(UPDATED_DESTINATION)
            .allerRetour(UPDATED_ALLER_RETOUR)
            .prix(UPDATED_PRIX)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        VolDTO volDTO = volMapper.toDto(updatedVol);

        restVolMockMvc
            .perform(put(ENTITY_API_URL_ID, volDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(volDTO)))
            .andExpect(status().isOk());

        // Validate the Vol in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVolToMatchAllProperties(updatedVol);
    }

    @Test
    @Transactional
    void putNonExistingVol() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vol.setId(longCount.incrementAndGet());

        // Create the Vol
        VolDTO volDTO = volMapper.toDto(vol);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVolMockMvc
            .perform(put(ENTITY_API_URL_ID, volDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(volDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vol in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVol() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vol.setId(longCount.incrementAndGet());

        // Create the Vol
        VolDTO volDTO = volMapper.toDto(vol);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(volDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vol in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVol() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vol.setId(longCount.incrementAndGet());

        // Create the Vol
        VolDTO volDTO = volMapper.toDto(vol);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVolMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(volDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vol in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVolWithPatch() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vol using partial update
        Vol partialUpdatedVol = new Vol();
        partialUpdatedVol.setId(vol.getId());

        partialUpdatedVol.allerRetour(UPDATED_ALLER_RETOUR);

        restVolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVol.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVol))
            )
            .andExpect(status().isOk());

        // Validate the Vol in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVolUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVol, vol), getPersistedVol(vol));
    }

    @Test
    @Transactional
    void fullUpdateVolWithPatch() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vol using partial update
        Vol partialUpdatedVol = new Vol();
        partialUpdatedVol.setId(vol.getId());

        partialUpdatedVol
            .origin(UPDATED_ORIGIN)
            .destination(UPDATED_DESTINATION)
            .allerRetour(UPDATED_ALLER_RETOUR)
            .prix(UPDATED_PRIX)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restVolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVol.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVol))
            )
            .andExpect(status().isOk());

        // Validate the Vol in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVolUpdatableFieldsEquals(partialUpdatedVol, getPersistedVol(partialUpdatedVol));
    }

    @Test
    @Transactional
    void patchNonExistingVol() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vol.setId(longCount.incrementAndGet());

        // Create the Vol
        VolDTO volDTO = volMapper.toDto(vol);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, volDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(volDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vol in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVol() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vol.setId(longCount.incrementAndGet());

        // Create the Vol
        VolDTO volDTO = volMapper.toDto(vol);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(volDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vol in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVol() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vol.setId(longCount.incrementAndGet());

        // Create the Vol
        VolDTO volDTO = volMapper.toDto(vol);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVolMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(volDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vol in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVol() throws Exception {
        // Initialize the database
        insertedVol = volRepository.saveAndFlush(vol);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vol
        restVolMockMvc.perform(delete(ENTITY_API_URL_ID, vol.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return volRepository.count();
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

    protected Vol getPersistedVol(Vol vol) {
        return volRepository.findById(vol.getId()).orElseThrow();
    }

    protected void assertPersistedVolToMatchAllProperties(Vol expectedVol) {
        assertVolAllPropertiesEquals(expectedVol, getPersistedVol(expectedVol));
    }

    protected void assertPersistedVolToMatchUpdatableProperties(Vol expectedVol) {
        assertVolAllUpdatablePropertiesEquals(expectedVol, getPersistedVol(expectedVol));
    }
}
