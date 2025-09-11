package az.cybernet.usermanagement.service.impl;

import az.cybernet.usermanagement.dto.event.UserApprovedEvent;
import az.cybernet.usermanagement.dto.request.ApproveUserRequest;
import az.cybernet.usermanagement.dto.response.ApproveUserResponse;
import az.cybernet.usermanagement.entity.Registration;
import az.cybernet.usermanagement.entity.User;
import az.cybernet.usermanagement.enums.RegistrationStatus;
import az.cybernet.usermanagement.enums.RegistrationType;
import az.cybernet.usermanagement.exception.RegistrationNotFoundException;
import az.cybernet.usermanagement.mapper.RegistrationMapper;
import az.cybernet.usermanagement.mapper.UserMapper;
import az.cybernet.usermanagement.service.NotificationProducerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationApprovalServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private RegistrationMapper registrationMapper;

    @Mock
    private NotificationProducerService notificationProducerService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegistrationApprovalServiceImpl approvalService;

//    @Test
//    void approveUser_shouldSucceed_whenUserAndRegistrationAreValid() {
//        UUID userId = UUID.randomUUID();
//        ApproveUserRequest request = new ApproveUserRequest();
//        request.setUserId(userId);
//
//        User pendingUser = User.builder()
//                .id(userId)
////                .approved(false)
//               // .dateOfBirth(LocalDate.of(2005, 10, 23))
//                .build();
//
//        Registration pendingRegistration = new Registration();
//        pendingRegistration.setId(UUID.randomUUID());
//        pendingRegistration.setTypeOfRegistration(RegistrationType.LEGAL_ENTITY);
//        pendingRegistration.setLegalEntityName("Test Corp");
//
//        when(userMapper.findById(userId)).thenReturn(Optional.of(pendingUser));
//        when(registrationMapper.findPendingRegistrationByUserId(userId)).thenReturn(Optional.of(pendingRegistration));
//        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword123");
//        when(userMapper.findByUserId(anyString())).thenReturn(Optional.empty()); // For unique ID generation
//
//        ApproveUserResponse response = approvalService.approveUser(request);
//
//        assertNotNull(response);
//        assertNotNull(response.getTaxId());
//        assertNotNull(response.getUserId());
//        assertEquals("Registration approved. You can log in with your credentials.", response.getMessage());
//
//        verify(userMapper).updateUser(any(User.class));
//        verify(registrationMapper).updateStatus(pendingRegistration.getId(), RegistrationStatus.APPROVED);
//        verify(notificationProducerService).sendUserApprovedNotification(any(UserApprovedEvent.class));
//    }
//
//    @Test
//    void approveUser_shouldThrowException_whenUserIsAlreadyApproved() {
//        UUID userId = UUID.randomUUID();
//        ApproveUserRequest request = new ApproveUserRequest();
//        request.setUserId(userId);
//
//        User approvedUser = User.builder().id(userId)
////                .approved(true)
//                .build();
//
//        when(userMapper.findById(userId)).thenReturn(Optional.of(approvedUser));
//        when(registrationMapper.findPendingRegistrationByUserId(userId)).thenReturn(Optional.of(new Registration()));
//
//        assertThrows(IllegalStateException.class, () -> approvalService.approveUser(request));
//
//        verify(userMapper, never()).updateUser(any());
//        verify(notificationProducerService, never()).sendUserApprovedNotification(any());
//    }
//
//    @Test
//    void approveUser_shouldThrowException_whenPendingRegistrationIsNotFound() {
//        UUID userId = UUID.randomUUID();
//        ApproveUserRequest request = new ApproveUserRequest();
//        request.setUserId(userId);
//
//        User pendingUser = User.builder().id(userId)
////                .approved(false)
//                .build();
//
//        when(userMapper.findById(userId)).thenReturn(Optional.of(pendingUser));
//        when(registrationMapper.findPendingRegistrationByUserId(userId)).thenReturn(Optional.empty());
//
//        assertThrows(RegistrationNotFoundException.class, () -> approvalService.approveUser(request));
//    }
//
//    @Test
//    void approveUser_shouldThrowException_whenDateOfBirthIsNull() {
//        UUID userId = UUID.randomUUID();
//        ApproveUserRequest request = new ApproveUserRequest();
//        request.setUserId(userId);
//
//     //   User userWithoutDob = User.builder().id(userId).approved(false).dateOfBirth(null).build();
//
//       // when(userMapper.findById(userId)).thenReturn(Optional.of(userWithoutDob));
//        when(registrationMapper.findPendingRegistrationByUserId(userId)).thenReturn(Optional.of(new Registration()));
//
//        assertThrows(IllegalStateException.class, () -> approvalService.approveUser(request));
//    }
//
//    @Test
//    void generateUniqueUserId_shouldThrowException_afterMaxAttempts() {
//        UUID userId = UUID.randomUUID();
//        ApproveUserRequest request = new ApproveUserRequest();
//        request.setUserId(userId);
////        User pendingUser = User.builder().id(userId).approved(false).dateOfBirth(LocalDate.now()).build();
//        Registration pendingRegistration = new Registration();
//        pendingRegistration.setTypeOfRegistration(RegistrationType.INDIVIDUAL);
//
////        when(userMapper.findById(userId)).thenReturn(Optional.of(pendingUser));
//        when(registrationMapper.findPendingRegistrationByUserId(userId)).thenReturn(Optional.of(pendingRegistration));
//
//        when(userMapper.findByUserId(anyString())).thenReturn(Optional.of(new User()));
//
//        assertThrows(RuntimeException.class, () -> approvalService.approveUser(request));
//    }
}