package az.cybernet.usermanagement.controller;

import az.cybernet.usermanagement.dto.request.ApproveUserRequest;
import az.cybernet.usermanagement.dto.response.ApproveUserResponse;
import az.cybernet.usermanagement.service.RegistrationApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/registration")
@RequiredArgsConstructor
public class RegistrationApprovalController {

    private final RegistrationApprovalService approvalService;

    @PostMapping("/approve")
    public ApproveUserResponse approve(@RequestBody ApproveUserRequest request) {
        return approvalService.approveUser(request);
    }
}
