package az.cybernet.auth.client;

import az.cybernet.auth.dto.request.SMSRequest;
import az.cybernet.auth.dto.response.IamasDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "integration")
public interface IntegrationClient {

    @GetMapping("/api/integration/v1/iamas/{pin}")
    IamasDto getPinData(@PathVariable("pin") String pin);

    @PostMapping("/api/integration/v1/sms/send")
    String sendSMS(@RequestBody SMSRequest request);
}