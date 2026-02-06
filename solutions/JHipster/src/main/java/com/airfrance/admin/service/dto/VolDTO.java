package com.airfrance.admin.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.airfrance.admin.domain.Vol} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VolDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 3)
    @Pattern(regexp = "^[A-Z]{3}$")
    private String origin;

    @NotNull
    @Size(min = 3, max = 3)
    @Pattern(regexp = "^[A-Z]{3}$")
    private String destination;

    @NotNull
    private Boolean allerRetour;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal prix;

    private Instant createdAt;

    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Boolean getAllerRetour() {
        return allerRetour;
    }

    public void setAllerRetour(Boolean allerRetour) {
        this.allerRetour = allerRetour;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VolDTO)) {
            return false;
        }

        VolDTO volDTO = (VolDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, volDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VolDTO{" +
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
