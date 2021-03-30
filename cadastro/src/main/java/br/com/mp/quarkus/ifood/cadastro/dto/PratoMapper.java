package br.com.mp.quarkus.ifood.cadastro.dto;

import br.com.mp.quarkus.ifood.cadastro.Prato;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface PratoMapper {

    PratoDTO toDTO(Prato prato);

    Prato toPrato(AdicionarPratoDTO dto);

    void toPrato(AtualizarPratoDTO dto, @MappingTarget Prato prato);
}
