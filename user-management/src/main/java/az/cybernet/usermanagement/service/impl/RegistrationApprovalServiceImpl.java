package az.cybernet.usermanagement.service.impl;

import az.cybernet.usermanagement.dto.request.ApproveUserRequest;
import az.cybernet.usermanagement.dto.response.ApproveUserResponse;
import az.cybernet.usermanagement.entity.User;
import az.cybernet.usermanagement.mapper.RegistrationMapper;
import az.cybernet.usermanagement.mapstruct.RegistrationMapstruct;
import az.cybernet.usermanagement.service.RegistrationApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class RegistrationApprovalServiceImpl implements RegistrationApprovalService {

    private final RegistrationMapper registrationMapper;
    private final RegistrationMapstruct registrationMapstruct;

    @Override
    public ApproveUserResponse approveUser(ApproveUserRequest request) {
        User user = registrationMapper.findById(request.getUserId());
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        String taxId = "VOEN" + Math.abs(ThreadLocalRandom.current().nextInt(99999999));
        String userId = String.format("%06d", ThreadLocalRandom.current().nextInt(999999));

        String rawPassword = user.getDateOfBirth().toString().replace("-", "");
        String hashedPassword = new BCryptPasswordEncoder().encode(rawPassword);

        user.setTaxId(taxId);
        user.setUserId(userId);
        user.setPassword(hashedPassword);
        user.setApproved(true);

        registrationMapper.updateUser(user);

        return ApproveUserResponse.builder()
                .taxId(user.getTaxId())
                .userId(user.getUserId())
                .message("Registration approved. You can log in with your credentials.")
                .build();
    }
}