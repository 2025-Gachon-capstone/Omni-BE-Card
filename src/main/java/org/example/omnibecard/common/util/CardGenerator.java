package org.example.omnibecard.common.util;

import java.security.SecureRandom;

public class CardGenerator {

    private static final SecureRandom random = new SecureRandom();

    // 16자리 카드 번호 생성
    public static String generateCardNumber() {
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    // 3자리 보안 코드 생성
    public static String generateSecurityCode() {
        StringBuilder sb = new StringBuilder(3);
        for (int i = 0; i < 3; i++) {
            sb.append(random.nextInt(10)); // 0~9
        }
        return sb.toString();
    }

}
