package az.cybernet.usermanagement.client;

import az.cybernet.usermanagement.dto.request.MessageRequest;
import az.cybernet.usermanagement.dto.response.IamasDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "integration")
public interface IntegrationClient {

    @GetMapping("/api/integration/v1/iamas/{pin}")
    IamasDto getPinData(@PathVariable("pin") String pin);

    @PostMapping("/api/integration/v1/messaging/sendSMS")
    String sendSMS(@RequestBody MessageRequest request);

    @PostMapping("/api/integration/v1/messaging/sendTG")
    String sendTG(@RequestBody MessageRequest request);
}