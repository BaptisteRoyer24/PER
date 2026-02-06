package com.airfrance.admin.service.mapper;

import static com.airfrance.admin.domain.VolAsserts.*;
import static com.airfrance.admin.domain.VolTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VolMapperTest {

    private VolMapper volMapper;

    @BeforeEach
    void setUp() {
        volMapper = new VolMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVolSample1();
        var actual = volMapper.toEntity(volMapper.toDto(expected));
        assertVolAllPropertiesEquals(expected, actual);
    }
}
