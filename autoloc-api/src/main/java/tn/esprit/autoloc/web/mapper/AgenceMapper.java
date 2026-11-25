package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.web.dto.AgenceDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AgenceMapper {

    AgenceDTO toDto(Agence agence);

    @Mapping(target = "vehicules", ignore = true)
    @Mapping(target = "employes", ignore = true)
    Agence toEntity(AgenceDTO dto);

    List<AgenceDTO> toDtoList(List<Agence> agences);

    List<Agence> toEntityList(List<AgenceDTO> dtos);
}