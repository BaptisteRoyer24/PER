package com.airfrance.admin.domain;

import static com.airfrance.admin.domain.OffreTestSamples.*;
import static com.airfrance.admin.domain.VolTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.airfrance.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OffreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Offre.class);
        Offre offre1 = getOffreSample1();
        Offre offre2 = new Offre();
        assertThat(offre1).isNotEqualTo(offre2);

        offre2.setId(offre1.getId());
        assertThat(offre1).isEqualTo(offre2);

        offre2 = getOffreSample2();
        assertThat(offre1).isNotEqualTo(offre2);
    }

    @Test
    void volTest() {
        Offre offre = getOffreRandomSampleGenerator();
        Vol volBack = getVolRandomSampleGenerator();

        offre.setVol(volBack);
        assertThat(offre.getVol()).isEqualTo(volBack);

        offre.vol(null);
        assertThat(offre.getVol()).isNull();
    }
}
