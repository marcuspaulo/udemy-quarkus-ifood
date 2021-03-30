package br.com.mp.quarkus.ifood.cadastro;

import br.com.mp.quarkus.ifood.cadastro.dto.AtualizarRestauranteDTO;
import br.com.mp.quarkus.ifood.cadastro.util.TokenUtils;
import com.github.database.rider.cdi.api.DBRider;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.specification.RequestSpecification;
import org.approvaltests.Approvals;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static javax.ws.rs.core.Response.Status.NO_CONTENT;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@QuarkusTest
@QuarkusTestResource(CadastroTestLifecycleManager.class)
public class RestauranteResourceTest {

    private String token;

    @BeforeEach
    public void gerarToken() throws Exception {
        token = TokenUtils.generateTokenString("src/test/resources/JWTProprietarioClaims.json", null);
    }

    @Test
    @DataSet("restaurantes-cenario-1.yml")
    public void testBuscarRestaurantes() {
        String resultado = given().log().all()
                .when()
                .get("/restaurantes")
                .then()
                .statusCode(200)
                .extract().asString();
        Approvals.verifyJson(resultado);
    }

    @Test
    @DataSet("restaurantes-cenario-1.yml")
    public void testAlterarRestaurante() {
        AtualizarRestauranteDTO dto = new AtualizarRestauranteDTO();
        dto.nomeFantasia = "novoNome";
        Long parameterValue = 123L;
        given()
                .with().pathParam("id", parameterValue)
                .body(dto)
                .when().put("/restaurantes/{id}")
                .then()
                .statusCode(NO_CONTENT.getStatusCode())
                .extract().asString();

        Restaurante findById = Restaurante.findById(parameterValue);

        //poderia testar todos os outros atribudos
        Assert.assertEquals(dto.nomeFantasia, findById.nome);

    }

    private RequestSpecification given() {
        try {
            this.gerarToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token));
    }
}
