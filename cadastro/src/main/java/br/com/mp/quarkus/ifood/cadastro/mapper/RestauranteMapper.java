package br.com.mp.quarkus.ifood.cadastro.mapper;

import br.com.mp.quarkus.ifood.cadastro.Restaurante;
import br.com.mp.quarkus.ifood.cadastro.dto.AdicionarRestauranteDTO;
import br.com.mp.quarkus.ifood.cadastro.dto.AtualizarRestauranteDTO;
import br.com.mp.quarkus.ifood.cadastro.dto.RestauranteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface RestauranteMapper {

    @Mapping(target = "nome", source = "nomeFantasia")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "localizacao.id", ignore = true)
    Restaurante toRestaurante(AdicionarRestauranteDTO dto);

    @Mapping(target = "nome", source = "nomeFantasia")
    void toRestaurante(AtualizarRestauranteDTO dto, @MappingTarget Restaurante restaurante);

    @Mapping(target = "nomeFantasia", source = "nome")
    //Exemplo de formatação.
    @Mapping(target = "dataCriacao", dateFormat = "dd/MM/yyyy HH:mm:ss")
    RestauranteDTO toRestauranteDTO(Restaurante restaurante);

}