package com.airfrance.admin.service.mapper;

import com.airfrance.admin.domain.Vol;
import com.airfrance.admin.service.dto.VolDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vol} and its DTO {@link VolDTO}.
 */
@Mapper(componentModel = "spring")
public interface VolMapper extends EntityMapper<VolDTO, Vol> {}
