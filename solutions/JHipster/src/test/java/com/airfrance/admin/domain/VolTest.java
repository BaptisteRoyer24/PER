package com.airfrance.admin.domain;

import static com.airfrance.admin.domain.VolTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.airfrance.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VolTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vol.class);
        Vol vol1 = getVolSample1();
        Vol vol2 = new Vol();
        assertThat(vol1).isNotEqualTo(vol2);

        vol2.setId(vol1.getId());
        assertThat(vol1).isEqualTo(vol2);

        vol2 = getVolSample2();
        assertThat(vol1).isNotEqualTo(vol2);
    }
}
