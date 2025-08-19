package az.cybernet.usermanagement.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApproveUserResponse {
    String taxId;
    String userId;
    String message;
}
