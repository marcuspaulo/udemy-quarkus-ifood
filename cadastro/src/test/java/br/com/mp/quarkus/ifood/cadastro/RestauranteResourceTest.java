package br.com.mp.quarkus.ifood.cadastro;

import com.github.database.rider.cdi.api.DBRider;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.approvaltests.Approvals;
import org.junit.Test;

import static io.restassured.RestAssured.given;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@QuarkusTest
@QuarkusTestResource(CadastroTestLifecycleManager.class)
public class RestauranteResourceTest {

    @Test
    @DataSet("restaurantes-cenario-1.yml")
    public void testBuscarRestautantes() {
        String resultado = given().log().all()
                .when()
                .get("/restaurantes")
                .then()
                .statusCode(200)
                .extract().asString();
        Approvals.verifyJson(resultado);
    }
}
