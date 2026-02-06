package com.airfrance.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.airfrance.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OffreDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OffreDTO.class);
        OffreDTO offreDTO1 = new OffreDTO();
        offreDTO1.setId(1L);
        OffreDTO offreDTO2 = new OffreDTO();
        assertThat(offreDTO1).isNotEqualTo(offreDTO2);
        offreDTO2.setId(offreDTO1.getId());
        assertThat(offreDTO1).isEqualTo(offreDTO2);
        offreDTO2.setId(2L);
        assertThat(offreDTO1).isNotEqualTo(offreDTO2);
        offreDTO1.setId(null);
        assertThat(offreDTO1).isNotEqualTo(offreDTO2);
    }
}
