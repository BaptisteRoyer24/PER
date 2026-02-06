package com.airfrance.admin.service.criteria;

import com.airfrance.admin.domain.enumeration.PrioriteOffre;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.airfrance.admin.domain.Offre} entity. This class is used
 * in {@link com.airfrance.admin.web.rest.OffreResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /offres?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OffreCriteria implements Serializable, Criteria {

    /**
     * Class for filtering PrioriteOffre
     */
    public static class PrioriteOffreFilter extends Filter<PrioriteOffre> {

        public PrioriteOffreFilter() {}

        public PrioriteOffreFilter(PrioriteOffreFilter filter) {
            super(filter);
        }

        @Override
        public PrioriteOffreFilter copy() {
            return new PrioriteOffreFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private PrioriteOffreFilter priorite;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private LongFilter volId;

    private Boolean distinct;

    public OffreCriteria() {}

    public OffreCriteria(OffreCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.priorite = other.optionalPriorite().map(PrioriteOffreFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(InstantFilter::copy).orElse(null);
        this.volId = other.optionalVolId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public OffreCriteria copy() {
        return new OffreCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public PrioriteOffreFilter getPriorite() {
        return priorite;
    }

    public Optional<PrioriteOffreFilter> optionalPriorite() {
        return Optional.ofNullable(priorite);
    }

    public PrioriteOffreFilter priorite() {
        if (priorite == null) {
            setPriorite(new PrioriteOffreFilter());
        }
        return priorite;
    }

    public void setPriorite(PrioriteOffreFilter priorite) {
        this.priorite = priorite;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<InstantFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new InstantFilter());
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public InstantFilter getUpdatedAt() {
        return updatedAt;
    }

    public Optional<InstantFilter> optionalUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    public InstantFilter updatedAt() {
        if (updatedAt == null) {
            setUpdatedAt(new InstantFilter());
        }
        return updatedAt;
    }

    public void setUpdatedAt(InstantFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getVolId() {
        return volId;
    }

    public Optional<LongFilter> optionalVolId() {
        return Optional.ofNullable(volId);
    }

    public LongFilter volId() {
        if (volId == null) {
            setVolId(new LongFilter());
        }
        return volId;
    }

    public void setVolId(LongFilter volId) {
        this.volId = volId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OffreCriteria that = (OffreCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(priorite, that.priorite) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(volId, that.volId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, priorite, createdAt, updatedAt, volId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OffreCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPriorite().map(f -> "priorite=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalVolId().map(f -> "volId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
