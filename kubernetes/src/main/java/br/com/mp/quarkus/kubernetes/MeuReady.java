package br.com.mp.quarkus.kubernetes;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

@Readiness
public class MeuReady implements HealthCheck {

	@Override
	public HealthCheckResponse call() {
        return HealthCheckResponse.named("AcessandoBanco")
        .withData("hostDoBanco", "local")
        .up().build();
	}
    
}
