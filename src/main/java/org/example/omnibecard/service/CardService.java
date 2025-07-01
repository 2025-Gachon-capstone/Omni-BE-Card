package org.example.omnibecard.service;

import org.example.omnibecard.dto.CardReqDto;
import org.example.omnibecard.dto.CardResDto;
import org.example.omnibecard.entity.Card;
import org.springframework.data.domain.Pageable;


public interface CardService {

    void createCard(CardReqDto.CreateCard createCardDto);
    void createCardByUser(CardReqDto.CreateCardByUser createCardDto,Long memberId);
    void verifyCard(Long memberId, String cardPassword);
    CardResDto.GetCard getCard(Long memberId);
    CardResDto.GetMemberId getMemberId(CardReqDto.GetMemberId getMemberIdDto);

}
