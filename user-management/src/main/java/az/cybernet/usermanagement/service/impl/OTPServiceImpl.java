package az.cybernet.usermanagement.service.impl;

import az.cybernet.usermanagement.service.IntegrationService;
import az.cybernet.usermanagement.service.OTPService;
import az.cybernet.usermanagement.util.OTPutil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OTPServiceImpl implements OTPService {

    private final OTPutil util;
    private final StringRedisTemplate stringRedisTemplate;
    private final IntegrationService integrationService;

    @Override
    public String sendOTP(String phone) {
        String otp = String.valueOf(util.generateOTP());
        stringRedisTemplate.opsForValue()
                .set("otp:login:" + phone,
                        util.hashOTP(otp),
                        5,
                        TimeUnit.MINUTES);

        return integrationService.sendSMS(phone, otp);
    }
}
