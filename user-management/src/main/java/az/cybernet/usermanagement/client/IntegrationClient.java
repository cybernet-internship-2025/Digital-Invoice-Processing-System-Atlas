package az.cybernet.usermanagement.client;

import az.cybernet.usermanagement.dto.response.IamasDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "integration")
public interface IntegrationClient {

    @GetMapping("/api/integration/v1/iamas/{pin}")
    IamasDto getPinData(@PathVariable("pin") String pin);
}