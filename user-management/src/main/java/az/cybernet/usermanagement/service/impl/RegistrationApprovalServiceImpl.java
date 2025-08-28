package az.cybernet.usermanagement.service.impl;

import az.cybernet.usermanagement.dto.event.UserApprovedEvent;
import az.cybernet.usermanagement.dto.request.ApproveUserRequest;
import az.cybernet.usermanagement.dto.response.ApproveUserResponse;
import az.cybernet.usermanagement.entity.Registration;
import az.cybernet.usermanagement.entity.User;
import az.cybernet.usermanagement.entity.UserDetails;
import az.cybernet.usermanagement.enums.RegistrationStatus;
import az.cybernet.usermanagement.exception.RegistrationNotFoundException;
import az.cybernet.usermanagement.exception.UserNotFoundException;
import az.cybernet.usermanagement.mapper.RegistrationMapper;
import az.cybernet.usermanagement.mapper.UserDetailsMapper;
import az.cybernet.usermanagement.mapper.UserMapper;
import az.cybernet.usermanagement.mapstruct.UserDetailsMapstruct;
import az.cybernet.usermanagement.service.RegistrationApprovalService;
import az.cybernet.usermanagement.service.NotificationProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class RegistrationApprovalServiceImpl implements RegistrationApprovalService {

    private final RegistrationMapper registrationMapper;
    private final UserMapper userMapper;
    private final UserDetailsMapstruct userDetailsMapstruct;
    private final NotificationProducerService notificationProducerService;
    private final PasswordEncoder passwordEncoder;

    private static final int MAX_USER_ID_GENERATION_ATTEMPTS = 10;
    private static final DateTimeFormatter PASSWORD_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String ORGANIZATION_CODE = "10";// placeholder
    private final UserDetailsMapper userDetailsMapper;

    @Override
    @Transactional
    public ApproveUserResponse approveUser(ApproveUserRequest request) {
        User user = userMapper.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found for approval: " + request.getUserId()));

        Registration registration = registrationMapper.findPendingRegistrationByUserId(user.getId())
                .orElseThrow(() -> new RegistrationNotFoundException("No pending registration found for user: " + user.getId()));

        LocalDate dob = request.getDateOfBirth();
        if (dob == null) {
            throw new IllegalStateException("Cannot approve user. Date of Birth is missing for user: " + user.getId());
        }

        String taxId = generateVoen(registration);
        String userId = generateUniqueUserId();

        String rawPassword = dob.format(PASSWORD_DATE_FORMAT);
        String hashedPassword = passwordEncoder.encode(rawPassword);

        user.setTaxId(taxId);
        user.setName(registration.getLegalEntityName());
        user.setPassword(hashedPassword);

        userMapper.updateUser(user);
        registrationMapper.updateStatus(registration.getId(), RegistrationStatus.APPROVED);

        UserDetails userDetails = userDetailsMapstruct.userToUserDetails(user, registration);

        userDetails.setUserId(userId);
        userDetails.setDateOfBirth(request.getDateOfBirth());

        userDetailsMapper.saveUserDetails(userDetails);

        UserApprovedEvent event = UserApprovedEvent.builder()
                .recipientUserId(user.getId())
                .newUserId(userDetails.getUserId())
                .newTaxId(user.getTaxId())
                .eventType("USER_REGISTRATION_APPROVED")
                .build();

        notificationProducerService.sendUserApprovedNotification(event);

        return ApproveUserResponse.builder()
                .taxId(user.getTaxId())
                .userId(userDetails.getUserId())
                .message("Registration approved. You can log in with your credentials.")
                .build();
    }

    private String generateVoen(Registration registration) {
        if (registration.getTypeOfRegistration() == null) {
            throw new IllegalStateException("Cannot generate VOEN. RegistrationType is missing for registration: " + registration.getId());
        }

        long randomPart = ThreadLocalRandom.current().nextLong(1_000_000, 10_000_000);

        String typeSuffix = switch (registration.getTypeOfRegistration()) {
            case INDIVIDUAL -> "2";
            case LEGAL_ENTITY -> "1";
        };

        String organizationCode = registration.getOrganizationId().toString();
        return organizationCode + randomPart + typeSuffix;
    }

    private String generateUniqueUserId() {
        for (int i = 0; i < MAX_USER_ID_GENERATION_ATTEMPTS; i++) {
            String potentialId = String.format("%06d", ThreadLocalRandom.current().nextInt(100_000, 1_000_000));
            if (userMapper.findByUserId(potentialId).isEmpty()) {
                return potentialId;
            }
        }
        throw new RuntimeException("Failed to generate a unique User ID after " + MAX_USER_ID_GENERATION_ATTEMPTS + " attempts.");
    }
}