package org.example.omnibecard.service;

import org.example.omnibecard.dto.CardReqDto;
import org.example.omnibecard.entity.Card;


public interface CardService {

    void createCard(CardReqDto.CreateCard createCardDto);
    void verifyCard(String loginId, String cardPassword);
    Card getCard(String loginId);
}
