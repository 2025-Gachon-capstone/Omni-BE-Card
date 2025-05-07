package org.example.omnibecard.converter;

import org.example.omnibecard.dto.BenefitResDto;
import org.example.omnibecard.dto.CardResDto;
import org.example.omnibecard.dto.MemberResDto;
import org.example.omnibecard.entity.Card;
import org.example.omnibecard.entity.CardBenefit;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CardConverter {

    private static String format(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

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

    public static CardResDto.GetCardForAdmin toGetCardForAdmin(
            Card card,
            MemberResDto.GetMemberList member,
            BenefitResDto.GetBatchBenefit benefitDto
    ) {
        return CardResDto.GetCardForAdmin.builder()
                .loginId(member.getLoginId())
                .memberName(member.getMemberName())
                .cardNumber(card.getCardNumber())
                .createdAt(format(card.getCreatedAt()))
                .updatedAt(format(card.getUpdatedAt()))
                .benefitTitle(benefitDto != null ? benefitDto.getTitle() : "혜택 없음")
                .status(member.getStatus())
                .build();
    }

    public static CardResDto.GetCardForAdminPage toGetCardForAdminPage(
            Page<Card> cardPage,
            Map<Long, MemberResDto.GetMemberList> memberMap,
            Map<Long, CardBenefit> latestBenefitMap,
            Map<Long, BenefitResDto.GetBatchBenefit> benefitMap
    ) {
        List<CardResDto.GetCardForAdmin> cardDtos = cardPage.stream()
                .map(card -> {
                    MemberResDto.GetMemberList member = memberMap.get(card.getMemberId());
                    if (member == null) {
                        return null; // 사용자 정보는 반드시 필요하므로 없으면 제외
                    }

                    CardBenefit benefit = latestBenefitMap.get(card.getCardId());
                    BenefitResDto.GetBatchBenefit benefitDto = (benefit != null) ? benefitMap.get(benefit.getBenefitId()) : null;

                    return toGetCardForAdmin(card, member, benefitDto);
                })
                .filter(Objects::nonNull)
                .toList();

        return CardResDto.GetCardForAdminPage.builder()
                .cards(cardDtos)
                .isFirst(cardPage.isFirst())
                .isLast(cardPage.isLast())
                .pageSize(cardPage.getSize())
                .totalElements(cardPage.getTotalElements())
                .build();
    }

}
