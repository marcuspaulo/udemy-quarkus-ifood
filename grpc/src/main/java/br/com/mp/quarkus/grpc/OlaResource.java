package br.com.mp.quarkus.grpc;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import br.com.mp.quarkus.grpc.proto.OlaRequest;
import br.com.mp.quarkus.grpc.proto.OlaResponse;
import br.com.mp.quarkus.grpc.proto.MutinyOlaServiceGrpc.MutinyOlaServiceStub;
import br.com.mp.quarkus.grpc.proto.OlaServiceGrpc.OlaServiceBlockingStub;

import io.quarkus.grpc.runtime.annotations.GrpcService;
import io.smallrye.mutiny.Uni;

@Path("/ola")
public class OlaResource {

    @Inject
    @GrpcService("ola-service")
    OlaServiceBlockingStub olaService;

    @Inject
    @GrpcService("ola-service")
    MutinyOlaServiceStub olaServiceMutiny;

    @GET
    @Path("/block/{nome}")
    public String getBlock(@PathParam("nome") String nome) {
        OlaRequest request = OlaRequest.newBuilder().setNome(nome).build();
		OlaResponse response = olaService.digaOla(request);
        return response.getMensagem()+", Quantidade: "+response.getQuantidadeDeChamadas();
    }

    @GET
    @Path("/reativo/{nome}")
    public Uni<String> getReativo(@PathParam("nome") String nome) {
        OlaRequest request = OlaRequest.newBuilder().setNome(nome).build();
        Uni<OlaResponse> response = olaServiceMutiny.digaOla(request);
        return response.onItem().apply(i -> i.getMensagem()+", Quantidade: "+i.getQuantidadeDeChamadas());
    }

}