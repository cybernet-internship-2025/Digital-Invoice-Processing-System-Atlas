package az.cybernet.usermanagement.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class ApproveUserRequest {
    UUID userId;
}
