package az.cybernet.auth.client;

import az.cybernet.auth.dto.response.UserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-management")
public interface UserClient {

    @GetMapping("/api/user-management/v1/users/{name}")
    UserInfoResponse getUserByName(@PathVariable("name") String name);
}
