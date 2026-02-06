package com.airfrance.admin.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Vol.
 */
@Entity
@Table(name = "vol")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 3)
    @Pattern(regexp = "^[A-Z]{3}$")
    @Column(name = "origin", length = 3, nullable = false)
    private String origin;

    @NotNull
    @Size(min = 3, max = 3)
    @Pattern(regexp = "^[A-Z]{3}$")
    @Column(name = "destination", length = 3, nullable = false)
    private String destination;

    @NotNull
    @Column(name = "aller_retour", nullable = false)
    private Boolean allerRetour;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "prix", precision = 21, scale = 2, nullable = false)
    private BigDecimal prix;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vol id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrigin() {
        return this.origin;
    }

    public Vol origin(String origin) {
        this.setOrigin(origin);
        return this;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return this.destination;
    }

    public Vol destination(String destination) {
        this.setDestination(destination);
        return this;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Boolean getAllerRetour() {
        return this.allerRetour;
    }

    public Vol allerRetour(Boolean allerRetour) {
        this.setAllerRetour(allerRetour);
        return this;
    }

    public void setAllerRetour(Boolean allerRetour) {
        this.allerRetour = allerRetour;
    }

    public BigDecimal getPrix() {
        return this.prix;
    }

    public Vol prix(BigDecimal prix) {
        this.setPrix(prix);
        return this;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Vol createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Vol updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vol)) {
            return false;
        }
        return getId() != null && getId().equals(((Vol) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vol{" +
            "id=" + getId() +
            ", origin='" + getOrigin() + "'" +
            ", destination='" + getDestination() + "'" +
            ", allerRetour='" + getAllerRetour() + "'" +
            ", prix=" + getPrix() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
