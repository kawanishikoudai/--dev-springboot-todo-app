package com.todo.api.service;

import com.todo.api.entity.Otp;
import com.todo.api.entity.User;
import com.todo.api.repository.OtpRepository;
import com.todo.api.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final EmailService emailService;

    public void sendOtp(String email) {
        String code = String.format("%06d", new Random().nextInt(999999));

        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setCode(code);
        otp.setExpiredAt(LocalDateTime.now().plusMinutes(5));
        otpRepository.save(otp);

        emailService.sendOtp(email, code);
    }

    public User verifyOtp(String email, String code) {
        Otp otp = otpRepository
            .findByEmailAndCode(email, code)
            .orElseThrow(() -> new RuntimeException("無効なコードです"));

        if (otp.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("コードの有効期限が切れています");
        }

        otpRepository.delete(otp);

        return userRepository.findByEmail(email).orElseGet(() -> {
            User user = new User();
            user.setEmail(email);
            return userRepository.save(user);
        });
    }
}
