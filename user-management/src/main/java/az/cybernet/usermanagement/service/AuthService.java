package az.cybernet.usermanagement.service;

public interface AuthService {
    String login(String pin, String phoneNumber);
}
