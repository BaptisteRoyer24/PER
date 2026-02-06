package com.airfrance.admin.service;

import com.airfrance.admin.domain.*; // for static metamodels
import com.airfrance.admin.domain.Vol;
import com.airfrance.admin.repository.VolRepository;
import com.airfrance.admin.service.criteria.VolCriteria;
import com.airfrance.admin.service.dto.VolDTO;
import com.airfrance.admin.service.mapper.VolMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Vol} entities in the database.
 * The main input is a {@link VolCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link VolDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VolQueryService extends QueryService<Vol> {

    private static final Logger LOG = LoggerFactory.getLogger(VolQueryService.class);

    private final VolRepository volRepository;

    private final VolMapper volMapper;

    public VolQueryService(VolRepository volRepository, VolMapper volMapper) {
        this.volRepository = volRepository;
        this.volMapper = volMapper;
    }

    /**
     * Return a {@link Page} of {@link VolDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VolDTO> findByCriteria(VolCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Vol> specification = createSpecification(criteria);
        return volRepository.findAll(specification, page).map(volMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VolCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Vol> specification = createSpecification(criteria);
        return volRepository.count(specification);
    }

    /**
     * Function to convert {@link VolCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Vol> createSpecification(VolCriteria criteria) {
        Specification<Vol> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Vol_.id),
                buildStringSpecification(criteria.getOrigin(), Vol_.origin),
                buildStringSpecification(criteria.getDestination(), Vol_.destination),
                buildSpecification(criteria.getAllerRetour(), Vol_.allerRetour),
                buildRangeSpecification(criteria.getPrix(), Vol_.prix),
                buildRangeSpecification(criteria.getCreatedAt(), Vol_.createdAt),
                buildRangeSpecification(criteria.getUpdatedAt(), Vol_.updatedAt)
            );
        }
        return specification;
    }
}
