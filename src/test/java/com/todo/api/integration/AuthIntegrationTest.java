package com.todo.api.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.todo.api.entity.Otp;
import com.todo.api.entity.User;
import com.todo.api.repository.OtpRepository;
import com.todo.api.service.AuthService;
import com.todo.api.service.JwtService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class AuthIntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private OtpRepository otpRepository;

    @MockitoBean
    private com.todo.api.service.EmailService emailService;

    @Test
    void 正しいOTPで認証できること() {
        String email = "test@example.com";

        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setCode("123456");
        otp.setExpiredAt(LocalDateTime.now().plusMinutes(5));
        otpRepository.save(otp);

        User user = authService.verifyOtp(email, "123456");

        assertThat(user.getEmail()).isEqualTo(email);
    }

    @Test
    void 無効なOTPで認証できないこと() {
        assertThatThrownBy(() ->
            authService.verifyOtp("test@example.com", "999999")
        )
            .isInstanceOf(RuntimeException.class)
            .hasMessage("無効なコードです");
    }

    @Test
    void 期限切れのOTPで認証できないこと() {
        String email = "test@example.com";

        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setCode("123456");
        otp.setExpiredAt(LocalDateTime.now().minusMinutes(1));
        otpRepository.save(otp);

        assertThatThrownBy(() -> authService.verifyOtp(email, "123456"))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("コードの有効期限が切れています");
    }

    @Test
    void JWTトークンからメールアドレスが取得できること() {
        String email = "test@example.com";
        String token = jwtService.generateToken(email);

        assertThat(jwtService.extractEmail(token)).isEqualTo(email);
    }
}
