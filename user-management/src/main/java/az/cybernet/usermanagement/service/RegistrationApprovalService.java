package az.cybernet.usermanagement.service;

import az.cybernet.usermanagement.dto.request.ApproveUserRequest;
import az.cybernet.usermanagement.dto.response.ApproveUserResponse;

public interface RegistrationApprovalService {
    ApproveUserResponse approveUser(ApproveUserRequest request);
}
