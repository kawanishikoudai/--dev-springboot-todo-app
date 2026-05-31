package com.todo.api.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${resend.api-key}")
    private String apiKey;

    public void sendOtp(String email, String code) {
        Resend resend = new Resend(apiKey);

        CreateEmailOptions params = CreateEmailOptions.builder()
            .from("onboarding@resend.dev")
            .to(email)
            .subject("ログインコード")
            .html(
                "<p>ログインコード: <strong>" +
                    code +
                    "</strong></p><p>5分以内に入力してください。</p>"
            )
            .build();

        try {
            resend.emails().send(params);
        } catch (ResendException e) {
            throw new RuntimeException("メール送信に失敗しました");
        }
    }
}
