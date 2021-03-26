package br.com.mp.quarkus.ifood.cadastro;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

import static javax.ws.rs.core.Response.Status.CREATED;

@Path("/restaurantes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "restaurante")
public class RestauranteResource {

    @GET
    public List<Restaurante> listar() {
        return Restaurante.listAll();
    }

    @POST
    @Transactional
    public Response adicionar(Restaurante restauranteDTO) {
        restauranteDTO.persist();

        return Response.status(CREATED).build();
    }

    @PUT
    @Path("{id}")
    @Transactional()
    public void atualizar(@PathParam("id") Long id, Restaurante restauranteDTO) {
        Optional<Restaurante> restauranteDatabase = Restaurante.findByIdOptional(id);

        if (restauranteDatabase.isEmpty()) {
            throw new NotFoundException();
        }

        Restaurante restaurante = restauranteDatabase.get();
        restaurante.nome = restauranteDTO.nome;
        restaurante.persist();
    }

    @DELETE
    @Path("{id}")
    @Transactional()
    public void adicionar(@PathParam("id") Long id) {
        Optional<Restaurante> restauranteDatabase = Restaurante.findByIdOptional(id);

        restauranteDatabase.ifPresentOrElse(Restaurante::delete, NotFoundException::new); //() -> {NotFoundException::new}
    }

    //Pratos

    @GET
    @Path("{idRestaurante}/pratos")
    @Tag(name = "prato")
    public List<Prato> listarPratos(@PathParam("idRestaurante") Long idRestaurante) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(idRestaurante);
        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante não existe");
        }

        return Prato.list("restaurante", restauranteOptional.get());
    }

    @POST
    @Path("/{idRestaurante}/pratos")
    @Transactional
    @Tag(name = "prato")
    public Response adicionarPrato(@PathParam("idRestaurante") Long idRestaurante, Prato pratoDTO) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(idRestaurante);
        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante não existe");
        }

        Prato prato = new Prato();
        prato.nome = pratoDTO.nome;
        prato.descricao = pratoDTO.descricao;
        prato.preco = pratoDTO.preco;

        prato.restaurante = restauranteOptional.get();
        prato.persist();
        return Response.status(CREATED).build();
    }

    @PUT
    @Path("{idRestaurante}/pratos/{id}")
    @Transactional()
    @Tag(name = "prato")
    public void atualizar(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id, Prato pratoDTO) {
        Optional<Restaurante> restauranteOptional = Prato.findByIdOptional(idRestaurante);
        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante não existe");
        }

        Optional<Prato> pratoOptional = Prato.findByIdOptional(id);

        if (pratoOptional.isEmpty()) {
            throw new NotFoundException("Prato não existe");
        }

        Prato prato = pratoOptional.get();
        prato.preco = pratoDTO.preco;
        prato.persist();
    }

    @DELETE
    @Path("{idRestaurante}/pratos/{id}")
    @Transactional()
    @Tag(name = "prato")
    public void delete(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id) {
        Optional<Restaurante> restauranteOptional = Prato.findByIdOptional(idRestaurante);
        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante não existe");
        }

        Optional<Prato> pratoOptional = Prato.findByIdOptional(id);
        pratoOptional.ifPresentOrElse(Prato::delete, () -> {
            throw new NotFoundException("Prato não existe");
        });
    }

}
