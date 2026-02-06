package com.airfrance.admin.web.rest;

import com.airfrance.admin.repository.VolRepository;
import com.airfrance.admin.service.VolQueryService;
import com.airfrance.admin.service.VolService;
import com.airfrance.admin.service.criteria.VolCriteria;
import com.airfrance.admin.service.dto.VolDTO;
import com.airfrance.admin.web.rest.errors.BadRequestAlertException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.airfrance.admin.domain.Vol}.
 */
@RestController
@RequestMapping("/api/vols")
public class VolResource {

    private static final Logger LOG = LoggerFactory.getLogger(VolResource.class);

    private static final String ENTITY_NAME = "vol";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VolService volService;

    private final VolRepository volRepository;

    private final VolQueryService volQueryService;

    public VolResource(VolService volService, VolRepository volRepository, VolQueryService volQueryService) {
        this.volService = volService;
        this.volRepository = volRepository;
        this.volQueryService = volQueryService;
    }

    /**
     * {@code POST  /vols} : Create a new vol.
     *
     * @param volDTO the volDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new volDTO, or with status {@code 400 (Bad Request)} if the vol has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VolDTO> createVol(@Valid @RequestBody VolDTO volDTO) throws URISyntaxException {
        LOG.debug("REST request to save Vol : {}", volDTO);
        if (volDTO.getId() != null) {
            throw new BadRequestAlertException("A new vol cannot already have an ID", ENTITY_NAME, "idexists");
        }
        volDTO = volService.save(volDTO);
        return ResponseEntity.created(new URI("/api/vols/" + volDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, volDTO.getId().toString()))
            .body(volDTO);
    }

    /**
     * {@code PUT  /vols/:id} : Updates an existing vol.
     *
     * @param id the id of the volDTO to save.
     * @param volDTO the volDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated volDTO,
     * or with status {@code 400 (Bad Request)} if the volDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the volDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VolDTO> updateVol(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody VolDTO volDTO)
        throws URISyntaxException {
        LOG.debug("REST request to update Vol : {}, {}", id, volDTO);
        if (volDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, volDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!volRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        volDTO = volService.update(volDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, volDTO.getId().toString()))
            .body(volDTO);
    }

    /**
     * {@code PATCH  /vols/:id} : Partial updates given fields of an existing vol, field will ignore if it is null
     *
     * @param id the id of the volDTO to save.
     * @param volDTO the volDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated volDTO,
     * or with status {@code 400 (Bad Request)} if the volDTO is not valid,
     * or with status {@code 404 (Not Found)} if the volDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the volDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VolDTO> partialUpdateVol(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VolDTO volDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Vol partially : {}, {}", id, volDTO);
        if (volDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, volDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!volRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VolDTO> result = volService.partialUpdate(volDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, volDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vols} : get all the vols.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vols in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VolDTO>> getAllVols(
        VolCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Vols by criteria: {}", criteria);

        Page<VolDTO> page = volQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vols/count} : count all the vols.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countVols(VolCriteria criteria) {
        LOG.debug("REST request to count Vols by criteria: {}", criteria);
        return ResponseEntity.ok().body(volQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /vols/:id} : get the "id" vol.
     *
     * @param id the id of the volDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the volDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VolDTO> getVol(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Vol : {}", id);
        Optional<VolDTO> volDTO = volService.findOne(id);
        return ResponseUtil.wrapOrNotFound(volDTO);
    }

    /**
     * {@code DELETE  /vols/:id} : delete the "id" vol.
     *
     * @param id the id of the volDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVol(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Vol : {}", id);
        volService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
