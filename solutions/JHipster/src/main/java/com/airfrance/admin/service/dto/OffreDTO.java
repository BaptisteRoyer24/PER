package com.airfrance.admin.service.dto;

import com.airfrance.admin.domain.enumeration.PrioriteOffre;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.airfrance.admin.domain.Offre} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OffreDTO implements Serializable {

    private Long id;

    @NotNull
    private PrioriteOffre priorite;

    private Instant createdAt;

    private Instant updatedAt;

    private VolDTO vol;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PrioriteOffre getPriorite() {
        return priorite;
    }

    public void setPriorite(PrioriteOffre priorite) {
        this.priorite = priorite;
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

    public VolDTO getVol() {
        return vol;
    }

    public void setVol(VolDTO vol) {
        this.vol = vol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OffreDTO)) {
            return false;
        }

        OffreDTO offreDTO = (OffreDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, offreDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OffreDTO{" +
            "id=" + getId() +
            ", priorite='" + getPriorite() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", vol=" + getVol() +
            "}";
    }
}
