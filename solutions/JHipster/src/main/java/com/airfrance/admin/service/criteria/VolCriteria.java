package com.airfrance.admin.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.airfrance.admin.domain.Vol} entity. This class is used
 * in {@link com.airfrance.admin.web.rest.VolResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vols?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VolCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter origin;

    private StringFilter destination;

    private BooleanFilter allerRetour;

    private BigDecimalFilter prix;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private Boolean distinct;

    public VolCriteria() {}

    public VolCriteria(VolCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.origin = other.optionalOrigin().map(StringFilter::copy).orElse(null);
        this.destination = other.optionalDestination().map(StringFilter::copy).orElse(null);
        this.allerRetour = other.optionalAllerRetour().map(BooleanFilter::copy).orElse(null);
        this.prix = other.optionalPrix().map(BigDecimalFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public VolCriteria copy() {
        return new VolCriteria(this);
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

    public StringFilter getOrigin() {
        return origin;
    }

    public Optional<StringFilter> optionalOrigin() {
        return Optional.ofNullable(origin);
    }

    public StringFilter origin() {
        if (origin == null) {
            setOrigin(new StringFilter());
        }
        return origin;
    }

    public void setOrigin(StringFilter origin) {
        this.origin = origin;
    }

    public StringFilter getDestination() {
        return destination;
    }

    public Optional<StringFilter> optionalDestination() {
        return Optional.ofNullable(destination);
    }

    public StringFilter destination() {
        if (destination == null) {
            setDestination(new StringFilter());
        }
        return destination;
    }

    public void setDestination(StringFilter destination) {
        this.destination = destination;
    }

    public BooleanFilter getAllerRetour() {
        return allerRetour;
    }

    public Optional<BooleanFilter> optionalAllerRetour() {
        return Optional.ofNullable(allerRetour);
    }

    public BooleanFilter allerRetour() {
        if (allerRetour == null) {
            setAllerRetour(new BooleanFilter());
        }
        return allerRetour;
    }

    public void setAllerRetour(BooleanFilter allerRetour) {
        this.allerRetour = allerRetour;
    }

    public BigDecimalFilter getPrix() {
        return prix;
    }

    public Optional<BigDecimalFilter> optionalPrix() {
        return Optional.ofNullable(prix);
    }

    public BigDecimalFilter prix() {
        if (prix == null) {
            setPrix(new BigDecimalFilter());
        }
        return prix;
    }

    public void setPrix(BigDecimalFilter prix) {
        this.prix = prix;
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
        final VolCriteria that = (VolCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(origin, that.origin) &&
            Objects.equals(destination, that.destination) &&
            Objects.equals(allerRetour, that.allerRetour) &&
            Objects.equals(prix, that.prix) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, origin, destination, allerRetour, prix, createdAt, updatedAt, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VolCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalOrigin().map(f -> "origin=" + f + ", ").orElse("") +
            optionalDestination().map(f -> "destination=" + f + ", ").orElse("") +
            optionalAllerRetour().map(f -> "allerRetour=" + f + ", ").orElse("") +
            optionalPrix().map(f -> "prix=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
