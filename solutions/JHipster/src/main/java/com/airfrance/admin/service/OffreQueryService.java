package com.airfrance.admin.service;

import com.airfrance.admin.domain.*; // for static metamodels
import com.airfrance.admin.domain.Offre;
import com.airfrance.admin.repository.OffreRepository;
import com.airfrance.admin.service.criteria.OffreCriteria;
import com.airfrance.admin.service.dto.OffreDTO;
import com.airfrance.admin.service.mapper.OffreMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Offre} entities in the database.
 * The main input is a {@link OffreCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OffreDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OffreQueryService extends QueryService<Offre> {

    private static final Logger LOG = LoggerFactory.getLogger(OffreQueryService.class);

    private final OffreRepository offreRepository;

    private final OffreMapper offreMapper;

    public OffreQueryService(OffreRepository offreRepository, OffreMapper offreMapper) {
        this.offreRepository = offreRepository;
        this.offreMapper = offreMapper;
    }

    /**
     * Return a {@link Page} of {@link OffreDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OffreDTO> findByCriteria(OffreCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Offre> specification = createSpecification(criteria);
        return offreRepository.findAll(specification, page).map(offreMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OffreCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Offre> specification = createSpecification(criteria);
        return offreRepository.count(specification);
    }

    /**
     * Function to convert {@link OffreCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Offre> createSpecification(OffreCriteria criteria) {
        Specification<Offre> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Offre_.id),
                buildSpecification(criteria.getPriorite(), Offre_.priorite),
                buildRangeSpecification(criteria.getCreatedAt(), Offre_.createdAt),
                buildRangeSpecification(criteria.getUpdatedAt(), Offre_.updatedAt),
                buildSpecification(criteria.getVolId(), root -> root.join(Offre_.vol, JoinType.LEFT).get(Vol_.id))
            );
        }
        return specification;
    }
}
