package az.cybernet.usermanagement.util;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class OTPutil {

        private final SecureRandom random;
        private final BCryptPasswordEncoder encoder;

        OTPutil() {
            this.random = new SecureRandom();
            this.encoder = new BCryptPasswordEncoder();
        }

        public int generateOTP() {
            return random.nextInt(1000000);
        }

        public String hashOTP(String otp) {
            return encoder.encode(otp);
        }

        public boolean verifyHash(String otp, String hashedOTP) {
            return BCrypt.checkpw(otp, hashedOTP);
        }
}
