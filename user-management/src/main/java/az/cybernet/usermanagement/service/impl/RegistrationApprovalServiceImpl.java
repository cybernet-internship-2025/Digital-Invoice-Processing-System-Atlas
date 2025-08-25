package az.cybernet.usermanagement.service.impl;

import az.cybernet.usermanagement.dto.event.UserApprovedEvent;
import az.cybernet.usermanagement.dto.request.ApproveUserRequest;
import az.cybernet.usermanagement.dto.response.ApproveUserResponse;
import az.cybernet.usermanagement.entity.User;
import az.cybernet.usermanagement.mapper.RegistrationMapper;
import az.cybernet.usermanagement.mapstruct.RegistrationMapstruct;
import az.cybernet.usermanagement.service.RegistrationApprovalService;
import az.cybernet.usermanagement.service.NotificationProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class RegistrationApprovalServiceImpl implements RegistrationApprovalService {

    private final RegistrationMapper registrationMapper;
    private final RegistrationMapstruct registrationMapstruct;
    private final NotificationProducerService notificationProducerService;

    @Override
    @Transactional
    public ApproveUserResponse approveUser(ApproveUserRequest request) {
        return registrationMapper.findById(request.getUserId())
                .map(user -> {
                    String taxId = "VOEN" + Math.abs(ThreadLocalRandom.current().nextInt(99999999));
                    String userId = String.format("%06d", ThreadLocalRandom.current().nextInt(999999));

                    LocalDate dob = user.getDateOfBirth();
                    if (dob == null) {
                        throw new RuntimeException("Date of Birth is missing for user: " + request.getUserId());
                    }

                    String rawPassword = dob.toString().replace("-", "");
                    String hashedPassword = new BCryptPasswordEncoder().encode(rawPassword);

                    user.setTaxId(taxId);
                    user.setUserId(userId);
                    user.setPassword(hashedPassword);
                    user.setApproved(true);

                    registrationMapper.updateUser(user);

                    UserApprovedEvent event = UserApprovedEvent.builder()
                            .recipientUserId(user.getId())
                            .newUserId(user.getUserId())
                            .newTaxId(user.getTaxId())
                            .eventType("USER_REGISTRATION_APPROVED")
                            .build();

                    notificationProducerService.sendUserApprovedNotification(event);

                    return ApproveUserResponse.builder()
                            .taxId(user.getTaxId())
                            .userId(user.getUserId())
                            .message("Registration approved. You can log in with your credentials.")
                            .build();
                })
                .orElseThrow(() -> new RuntimeException("User not found: " + request.getUserId()));
    }
}