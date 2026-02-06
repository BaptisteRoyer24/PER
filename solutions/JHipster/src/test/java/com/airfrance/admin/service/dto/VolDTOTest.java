package com.airfrance.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.airfrance.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VolDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VolDTO.class);
        VolDTO volDTO1 = new VolDTO();
        volDTO1.setId(1L);
        VolDTO volDTO2 = new VolDTO();
        assertThat(volDTO1).isNotEqualTo(volDTO2);
        volDTO2.setId(volDTO1.getId());
        assertThat(volDTO1).isEqualTo(volDTO2);
        volDTO2.setId(2L);
        assertThat(volDTO1).isNotEqualTo(volDTO2);
        volDTO1.setId(null);
        assertThat(volDTO1).isNotEqualTo(volDTO2);
    }
}
