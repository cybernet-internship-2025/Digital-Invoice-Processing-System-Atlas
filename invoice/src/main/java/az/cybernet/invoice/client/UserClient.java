package az.cybernet.invoice.client;

import az.cybernet.invoice.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-management", url = "${user.service.url}")
public interface UserClient {

    @GetMapping("/v1/user-management/{id}")
    UserResponse getUserById(@PathVariable("id") UUID id);
}
