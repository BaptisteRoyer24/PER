package com.airfrance.admin.service.mapper;

import com.airfrance.admin.domain.Offre;
import com.airfrance.admin.domain.Vol;
import com.airfrance.admin.service.dto.OffreDTO;
import com.airfrance.admin.service.dto.VolDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Offre} and its DTO {@link OffreDTO}.
 */
@Mapper(componentModel = "spring")
public interface OffreMapper extends EntityMapper<OffreDTO, Offre> {
    @Mapping(target = "vol", source = "vol", qualifiedByName = "volOrigin")
    OffreDTO toDto(Offre s);

    @Named("volOrigin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "origin", source = "origin")
    VolDTO toDtoVolOrigin(Vol vol);
}
