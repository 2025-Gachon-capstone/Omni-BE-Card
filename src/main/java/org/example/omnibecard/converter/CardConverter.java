package org.example.omnibecard.converter;

import org.example.omnibecard.dto.CardResDto;
import org.example.omnibecard.entity.Card;

public class CardConverter {

    public static CardResDto.GetCard toGetCard(Card card) {

        return CardResDto.GetCard.builder()
                .cardId(card.getCardId())
                .cardNumber(card.getCardNumber())
                .memberName(card.getMemberName())
                .securityCode(card.getSecurityCode())
                .createdAt(card.getCreatedAt())
                .expired(card.getExpiredDate())
                .build();

    }
}
